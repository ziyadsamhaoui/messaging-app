package messagerie.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import messagerie.application.enums.GroupRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_participants",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"conversationId", "userId"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
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
