package messagerie.application.service;
import jakarta.transaction.Transactional;
import messagerie.application.enums.ConversationType;
import messagerie.application.entity.ConversationEntity;
import messagerie.application.entity.ConversationParticipantEntity;
import messagerie.application.enums.GroupRole;
import messagerie.application.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public ConversationService(ConversationRepository conversationRepository,
                               ConversationParticipantRepository participantRepository,
                               UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ConversationEntity createPrivateConversation(Long creatorId, String targetUsername){

        if (!userRepository.existsById(creatorId)) {
            throw new RuntimeException("User not found");
        }

        // find the target user by username
        // Opt = optional, class that either contains something or contains nothing, to avoid NullPointerException.
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

        ConversationEntity conversation = new ConversationEntity();
        conversation.setType(ConversationType.PRIVATE);
        conversation.setCreatedAt(LocalDateTime.now());

        conversation = conversationRepository.save(conversation);

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

        return conversation;
    }
}
