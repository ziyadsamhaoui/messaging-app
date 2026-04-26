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

    // nextCursor is the conversationId (as String) of the last item in the current page.
    private String nextCursor;
}

