package messagerie.application.controller;

import messagerie.application.dto.MessagePageDTO;
import messagerie.application.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/conversations/{conversationId}/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<MessagePageDTO> getMessages(
            @PathVariable Long conversationId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int limit) {
        MessagePageDTO page = messageService.getMessages(conversationId, cursor, limit);
        return ResponseEntity.ok(page);
    }
}
