package messagerie.application.controller;

import messagerie.application.dto.MessageDTO;
import messagerie.application.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations/{conversationId}/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessages(
            @PathVariable Long conversationId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int limit) {
        List<MessageDTO> messages = messageService.getMessages(conversationId, cursor, limit);
        return ResponseEntity.ok(messages);
    }
}
