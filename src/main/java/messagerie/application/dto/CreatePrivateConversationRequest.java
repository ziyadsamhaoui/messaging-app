package messagerie.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePrivateConversationRequest {
    private Long creatorId;
    private String targetUsername;
}

