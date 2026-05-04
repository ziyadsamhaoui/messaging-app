package messagerie.application.controller;

import messagerie.application.dto.MessagePageDTO;
import messagerie.application.dto.MessageDTO;
import messagerie.application.service.MessageService;
import messagerie.application.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.security.Principal;
import java.util.Map;


@RestController
@RequestMapping("/api/conversations/{conversationId}/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageService messageService, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public ResponseEntity<MessagePageDTO> getMessages(
            @PathVariable Long conversationId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int limit) {
        MessagePageDTO page = messageService.getMessages(conversationId, cursor, limit);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<MessageDTO> postMessage(
            @PathVariable Long conversationId,
            @RequestBody Map<String, String> body,
            Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        String content = body.get("content");
        if (content == null) return ResponseEntity.badRequest().build();

        var optUser = userRepository.findByUsername(principal.getName());
        if (optUser.isEmpty()) return ResponseEntity.status(401).build();
        Long senderId = optUser.get().getUserId();

        var saved = messageService.sendMessage(senderId, conversationId, content);
        MessageDTO dto = new MessageDTO(saved.getMessageId(), saved.getConversationId(), saved.getUserId(), saved.getContent(), saved.getCreatedAt());
        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId, dto);
        return ResponseEntity.ok(dto);
    }
}
