package messagerie.application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import messagerie.application.enums.ConversationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
@Getter
@Setter
public class ConversationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConversationType type;

    private LocalDateTime createdAt;

    public ConversationEntity() {}

    public ConversationEntity(ConversationType type, String name) {
        this.type = type;
        this.createdAt = LocalDateTime.now(); }

    public Long getConversationId() { return conversationId; }
    public ConversationType getType() { return type; }
    public LocalDateTime getCreatedAt() { return createdAt; }

}

