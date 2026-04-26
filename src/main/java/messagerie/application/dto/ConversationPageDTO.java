package messagerie.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationPageDTO {
    private List<ConversationDTO> items;

    // nextCursor is the conversationId of the last item in the current page.
    // Clients should pass this value (numeric) as the `cursor` parameter to fetch older items.
    private Long nextCursor;
}

