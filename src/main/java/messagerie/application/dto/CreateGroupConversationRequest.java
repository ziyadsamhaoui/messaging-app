package messagerie.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupConversationRequest {
    private Long creatorId;
    private List<String> participantUsernames;
    // optional name for the group conversation
    private String name;
}

