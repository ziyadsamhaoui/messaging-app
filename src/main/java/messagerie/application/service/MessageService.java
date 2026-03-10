package messagerie.application.service;

import jakarta.transaction.Transactional;
import messagerie.application.repository.ConversationRepository;
import messagerie.application.entity.MessageEntity;
import messagerie.application.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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


        //MAKE IT SIMILAR TO CONVERSATIONSERVICE
        userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        boolean isParticipant =
                participantRepository
                        .existsByConversationIdAndUserId(conversationId, senderId);

        if(!isParticipant) {
            throw new RuntimeException("User not part of conversation");
        }

        MessageEntity message = new MessageEntity();

        message.setUserId(senderId);
        message.setConversationId(conversationId);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(message);
    }
}
