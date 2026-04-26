package messagerie.application.service;

import jakarta.transaction.Transactional;
import messagerie.application.repository.ConversationRepository;
import messagerie.application.entity.MessageEntity;
import messagerie.application.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import messagerie.application.dto.MessageDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.format.DateTimeParseException;

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
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check conversation exists
        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // check sender is participant of conversation
        boolean isParticipant =
                participantRepository
                        .existsByConversationIdAndUserId(conversationId, senderId);
        if(!isParticipant) {
            throw new RuntimeException("User not part of conversation");
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

    public List<MessageDTO> getMessages(Long conversationId, String cursor, int limit) {
        Pageable pageable = PageRequest.of(0, Math.max(1, limit));
        List<MessageEntity> entities;

        if (cursor == null) {
            entities = messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);
        } else {
            // try parse cursor as ISO date-time, fallback to epoch millis
            try {
                LocalDateTime cursorDate = LocalDateTime.parse(cursor);
                entities = messageRepository.findByConversationIdAndCreatedAtLessThanOrderByCreatedAtDesc(conversationId, cursorDate, pageable);
            } catch (DateTimeParseException ex) {
                try {
                    long epoch = Long.parseLong(cursor);
                    LocalDateTime cursorDate = LocalDateTime.ofEpochSecond(epoch/1000, 0, java.time.ZoneOffset.UTC);
                    entities = messageRepository.findByConversationIdAndCreatedAtLessThanOrderByCreatedAtDesc(conversationId, cursorDate, pageable);
                } catch (Exception e) {
                    // fallback to latest
                    entities = messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);
                }
            }
        }

        return entities.stream()
                .map(e -> new MessageDTO(e.getMessageId(), e.getConversationId(), e.getUserId(), e.getContent(), e.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
