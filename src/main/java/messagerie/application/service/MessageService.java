package messagerie.application.service;

import jakarta.transaction.Transactional;
import messagerie.application.repository.ConversationRepository;
import messagerie.application.entity.MessageEntity;
import messagerie.application.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import messagerie.application.dto.MessageDTO;
import messagerie.application.exception.NotFoundException;
import messagerie.application.exception.ForbiddenException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import messagerie.application.dto.MessagePageDTO;

@Service
public class MessageService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public MessageService(ConversationRepository conversationRepository,
                          ConversationParticipantRepository participantRepository,
                          UserRepository userRepository,
                          MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public MessageEntity sendMessage(Long senderId, Long conversationId, String content) {

        // check sender exists
        userRepository.findById(senderId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // check conversation exists
        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation not found"));

        // check sender is participant of conversation
        boolean isParticipant =
                participantRepository
                        .existsByConversationIdAndUserId(conversationId, senderId);
        if(!isParticipant) {
            throw new ForbiddenException("User not part of conversation");
        }

        // create and save message
        MessageEntity message = MessageEntity.builder()
                 .conversationId(conversationId)
                 .userId(senderId)
                 .content(content)
                 .createdAt(LocalDateTime.now())
                 .build();

        return messageRepository.save(message);
    }

    public MessagePageDTO getMessages(Long conversationId, Long cursor, int limit) {
        int pageSize = Math.max(1, limit);
        int fetchSize = pageSize + 1; // fetch one extra item to determine whether another page exists

        Pageable pageable = PageRequest.of(0, fetchSize);
        List<MessageEntity> entities;

        if (cursor == null) {
            // no cursor => get latest messages ordered by id desc
            entities = messageRepository.findByConversationIdOrderByMessageIdDesc(conversationId, pageable);
        } else {
            // get messages with id less than cursor id => older messages
            entities = messageRepository.findByConversationIdAndMessageIdLessThanOrderByMessageIdDesc(conversationId, cursor, pageable);
        }

        List<MessageDTO> dtoList = entities.stream()
                .map(e -> new MessageDTO(e.getMessageId(), e.getConversationId(), e.getUserId(), e.getContent(), e.getCreatedAt()))
                .collect(Collectors.toList());

        // Keep only the requested page size and compute nextCursor using limit+1 strategy.
        boolean hasMore = dtoList.size() > pageSize;
        if (hasMore) {
            dtoList = dtoList.subList(0, pageSize);
        }

        // compute nextCursor: if hasMore, it is the id of the last returned item.
        Long nextCursor = null;
        if (hasMore && !dtoList.isEmpty()) {
            MessageDTO last = dtoList.get(dtoList.size() - 1);
            nextCursor = last.getMessageId();
        }

        return new MessagePageDTO(dtoList, nextCursor);
    }
}
