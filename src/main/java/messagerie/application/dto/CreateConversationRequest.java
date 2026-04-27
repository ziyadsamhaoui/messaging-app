package messagerie.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import messagerie.application.enums.ConversationType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateConversationRequest {
    private ConversationType type; // PRIVATE or GROUP

    // private conversation
    private String targetUsername;

    // group conversation
    private List<String> participantUsernames;
    private String name;
}

