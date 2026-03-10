package messagerie.application.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="messages")
@Getter
@Setter
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    private Long conversationId;
    private Long userId;

    @Column(nullable = false, length = 2000)
    private String content;
    private LocalDateTime createdAt;
    private boolean edited;
    private boolean deleted;

    public MessageEntity() {}

    public MessageEntity(Long conversationId, Long userId, String content) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.edited = false;
        this.deleted = false;
    }

    public void editMessage(String newContent) {
        if (deleted) {
            throw new IllegalStateException("Cannot edit a deleted message");
        }
        this.content = newContent;
        this.edited = true;
    }

    public void softDelete() {
        this.deleted = true;
    }
}

