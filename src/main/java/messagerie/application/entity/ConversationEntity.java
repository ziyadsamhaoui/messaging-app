package messagerie.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import messagerie.application.enums.ConversationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConversationType type;

    private String name;
    @Column(unique = true)
    private String privateKey;

    private LocalDateTime createdAt;

    public ConversationEntity(ConversationType type, String name) {
        this.type = type;
        this.name = name;
        this.createdAt = LocalDateTime.now(); }

}
