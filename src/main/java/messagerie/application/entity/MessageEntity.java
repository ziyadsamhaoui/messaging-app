package messagerie.application.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name="messages")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    // Full-args constructor used by some parts of the codebase/tests
    // (replaced by Lombok @AllArgsConstructor but kept convenience constructors below)

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
