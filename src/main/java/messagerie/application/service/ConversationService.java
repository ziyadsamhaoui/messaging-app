package messagerie.application.service;
import jakarta.transaction.Transactional;
import messagerie.application.dto.ConversationDTO;
import messagerie.application.enums.ConversationType;
import messagerie.application.entity.ConversationEntity;
import messagerie.application.entity.ConversationParticipantEntity;
import messagerie.application.enums.GroupRole;
import messagerie.application.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import org.springframework.dao.DataIntegrityViolationException;
import messagerie.application.dto.ConversationPageDTO;


@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ConversationService(ConversationRepository conversationRepository,
                               ConversationParticipantRepository participantRepository,
                               UserRepository userRepository,
                               SimpMessagingTemplate messagingTemplate) {
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public ConversationEntity createPrivateConversation(Long creatorId, String targetUsername){
        // keep existing signature for backwards compatibility
        return createPrivateConversation(creatorId, targetUsername, null);
    }


    @Transactional
    public ConversationEntity createPrivateConversation(Long creatorId, String targetUsername, String name){

        if (!userRepository.existsById(creatorId)) {
            throw new RuntimeException("User not found");
        }

        // find the target user by username
        var targetOpt = userRepository.findByUsername(targetUsername);
        if (targetOpt.isEmpty()) {
            throw new RuntimeException("Target user not found");
        }

        var targetUser = targetOpt.get();
        Long targetId = targetUser.getUserId();

        if (creatorId.equals(targetId)) {
            throw new RuntimeException("Cannot create a private conversation with yourself");
        }

        // check if a private conversation already exists between the two users
        Optional<ConversationEntity> existingConversation =
                conversationRepository.findPrivateConversation(creatorId, targetId);
        if(existingConversation.isPresent()){
            return existingConversation.get();
        }

        // create new conversation and set optional name
        ConversationEntity conversation = new ConversationEntity();
        conversation.setType(ConversationType.PRIVATE);
        conversation.setName(name);
        conversation.setCreatedAt(LocalDateTime.now());

        // set a deterministic privateKey for uniqueness (sorted user ids) to avoid race creating duplicates
        long a = Math.min(creatorId, targetId);
        long b = Math.max(creatorId, targetId);
        conversation.setPrivateKey(a + ":" + b);

        try {
            conversation = conversationRepository.save(conversation);
        } catch (DataIntegrityViolationException ex) {
            // Another concurrent request likely created the private conversation. Try to find it and return existing.
            var existing = conversationRepository.findPrivateConversation(creatorId, targetId);
            if (existing.isPresent()) {
                return existing.get();
            }
            throw ex;
        }

        // create participant for creator
        ConversationParticipantEntity creatorParticipant = new ConversationParticipantEntity();
        creatorParticipant.setUserId(creatorId);
        creatorParticipant.setConversationId(conversation.getConversationId());
        creatorParticipant.setJoinedAt(LocalDateTime.now());
        creatorParticipant.setLastReadMessageId(null);
        creatorParticipant.setRole(GroupRole.MEMBER);

        // create participant for target user
        ConversationParticipantEntity targetParticipant = new ConversationParticipantEntity();
        targetParticipant.setUserId(targetId);
        targetParticipant.setConversationId(conversation.getConversationId());
        targetParticipant.setJoinedAt(LocalDateTime.now());
        targetParticipant.setLastReadMessageId(null);
        targetParticipant.setRole(GroupRole.MEMBER);

        participantRepository.save(creatorParticipant);
        participantRepository.save(targetParticipant);

        // Notify via websocket that a conversation was created
        try {
            messagingTemplate.convertAndSend("/topic/conversations", toDTO(conversation));
        } catch (Exception ignore) {}

        return conversation;
    }


    @Transactional
    public ConversationEntity createGroupConversation(Long creatorId, List<String> participantUsernames) {
        // preserve old behavior without a group name
        return createGroupConversation(creatorId, participantUsernames, null);
    }


    @Transactional
    public ConversationEntity createGroupConversation(Long creatorId, List<String> participantUsernames, String name) {

        if (!userRepository.existsById(creatorId)) {
            throw new RuntimeException("Creator user not found");
        }

        // Normalize input list into a set of unique usernames
        Set<String> usernames = participantUsernames == null ? new HashSet<>() : new HashSet<>(participantUsernames);

        // Ensure creator is included
        var creatorOpt = userRepository.findById(creatorId);
        if (creatorOpt.isEmpty()) {
            throw new RuntimeException("Creator user not found");
        }
        var creator = creatorOpt.get();
        usernames.add(creator.getUsername());

        // Resolve usernames to user entities
        List<Long> userIds = new java.util.ArrayList<>();
        List<String> missing = new java.util.ArrayList<>();

        for (String uname : usernames) {
            if (uname == null || uname.isBlank()) continue;
            var uOpt = userRepository.findByUsername(uname);
            if (uOpt.isPresent()) {
                userIds.add(uOpt.get().getUserId());
            } else {
                missing.add(uname);
            }
        }

        if (!missing.isEmpty()) {
            throw new RuntimeException("Some users not found: " + String.join(",", missing));
        }

        // Need at least 2 participants for a group
        if (userIds.size() < 2) {
            throw new RuntimeException("A group conversation requires at least 2 participants");
        }

        // create conversation and set optional display name
        ConversationEntity conversation = new ConversationEntity();
        conversation.setType(ConversationType.GROUP);
        conversation.setName(name);
        conversation.setCreatedAt(LocalDateTime.now());

        conversation = conversationRepository.save(conversation);

        // create participants; creator becomes ADMIN
        java.util.List<ConversationParticipantEntity> participants = new java.util.ArrayList<>();
        for (Long uid : userIds) {
            ConversationParticipantEntity part = new ConversationParticipantEntity();
            part.setConversationId(conversation.getConversationId());
            part.setUserId(uid);
            part.setJoinedAt(LocalDateTime.now());
            part.setLastReadMessageId(null);
            part.setRole(uid.equals(creatorId) ? GroupRole.ADMIN : GroupRole.MEMBER);
            participants.add(part);
        }

        participantRepository.saveAll(participants);

        // Notify via websocket that a group conversation was created
        try {
            messagingTemplate.convertAndSend("/topic/conversations", toDTO(conversation));
        } catch (Exception ignore) {}

        return conversation;
    }

    public ConversationPageDTO getConversations(Long cursor, int limit) {
        // Use id-based cursor pagination. The cursor is expected to be the last-seen conversationId (as Long).
        // We fetch conversations with conversationId < cursor ordered by conversationId desc.
        // To determine if there's a next page, we fetch limit+1 items and, if present, return a nextCursor.

        int pageSize = Math.max(1, limit);
        int fetchSize = pageSize + 1; // fetch one extra to detect if there is another page

        var pageable = org.springframework.data.domain.PageRequest.of(0, fetchSize, org.springframework.data.domain.Sort.by("conversationId").descending());
        java.util.List<ConversationEntity> entities;

        if (cursor == null) {
            // No cursor -> return the first page (latest conversations)
            entities = conversationRepository.findAll(pageable).getContent();
        } else {
            entities = conversationRepository.findByConversationIdLessThanOrderByConversationIdDesc(cursor, pageable);
        }

        // Determine if we have more results than pageSize
        boolean hasMore = entities.size() > pageSize;
        java.util.List<ConversationEntity> pageItems = hasMore ? entities.subList(0, pageSize) : entities;

        // Map to DTOs, including participants info
        List<ConversationDTO> dtoList = pageItems.stream().map(c -> {
            var participants = participantRepository.findByConversationId(c.getConversationId());
            var userDtos = participants.stream().map(p -> {
                var userOpt = userRepository.findById(p.getUserId());
                if (userOpt.isPresent()) {
                    var u = userOpt.get();
                    return new messagerie.application.dto.UserDTO(u.getUserId(), u.getUsername(), u.getDisplayName(), u.getEmail(), u.getCreatedAt());
                } else {
                    return new messagerie.application.dto.UserDTO();
                }
            }).toList();

            // include conversation name in DTO mapping (matches ConversationDTO all-args constructor)
            return new ConversationDTO(c.getConversationId(), c.getType(), c.getName(), c.getCreatedAt(), userDtos);
        }).toList();

        // nextCursor is the id of the last item in this page (so clients can pass it to get older items).
        Long nextCursor = null;
        if (hasMore && !pageItems.isEmpty()) {
            Long lastId = pageItems.get(pageItems.size() - 1).getConversationId();
            nextCursor = lastId;
        }

        return new ConversationPageDTO(dtoList, nextCursor);
    }

    /**
     * Map ConversationEntity to ConversationDTO, including participant user information.
     */
    public ConversationDTO toDTO(ConversationEntity c) {
        var participants = participantRepository.findByConversationId(c.getConversationId());
        var userDtos = participants.stream().map(p -> {
            var userOpt = userRepository.findById(p.getUserId());
            if (userOpt.isPresent()) {
                var u = userOpt.get();
                return new messagerie.application.dto.UserDTO(u.getUserId(), u.getUsername(), u.getDisplayName(), u.getEmail(), u.getCreatedAt());
            } else {
                return new messagerie.application.dto.UserDTO();
            }
        }).toList();

        return new ConversationDTO(c.getConversationId(), c.getType(), c.getName(), c.getCreatedAt(), userDtos);
    }
}
