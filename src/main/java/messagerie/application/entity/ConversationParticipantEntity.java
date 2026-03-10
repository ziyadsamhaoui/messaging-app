package messagerie.application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import messagerie.application.enums.GroupRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_participants",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"conversationId", "userId"})
        })
@Setter
@Getter
public class ConversationParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long conversationId;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private GroupRole role;
    private LocalDateTime joinedAt;
    private Long lastReadMessageId;

    public ConversationParticipantEntity() {}

    public ConversationParticipantEntity(Long conversationId, Long userId, GroupRole role) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }

    public void updateLastReadMessage(Long messageId) {
        this.lastReadMessageId = messageId;
    }
}
