package messagerie.application.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Long messageId;
    private Long conversationId;
    private Long senderId;
    private String senderUsername;
    private String targetUsername; // optional, used to create/find private conversations
    private String content;
    private LocalDateTime createdAt;
}
