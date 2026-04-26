package messagerie.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePageDTO {
    private List<MessageDTO> items;

    // nextCursor is the messageId of the last item in the current page.
    // Use this value as the `cursor` parameter (numeric) to fetch the next (older) page.
    private Long nextCursor;
}

