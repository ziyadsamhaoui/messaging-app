package messagerie.application.controller;

import messagerie.application.dto.ConversationDTO;
import messagerie.application.dto.ConversationPageDTO;
import messagerie.application.dto.CreateConversationRequest;
import messagerie.application.entity.ConversationEntity;
import messagerie.application.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// ...existing code...

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping
    public ResponseEntity<ConversationPageDTO> getConversations(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int limit) {

        // Manually enforce bounds for limit to avoid relying on validation annotations that may not be present.
        int boundedLimit = Math.max(1, Math.min(limit, 50));
        ConversationPageDTO page = conversationService.getConversations(cursor, boundedLimit);
        return ResponseEntity.ok(page);
    }

    // Unified create conversation endpoint. Body must include `type` = PRIVATE or GROUP.
    @PostMapping
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody CreateConversationRequest req) {
        if (req == null || req.getType() == null) {
            return ResponseEntity.badRequest().build();
        }

        ConversationEntity created;
        if (req.getType() == messagerie.application.enums.ConversationType.PRIVATE) {
            if (req.getTargetUsername() == null || req.getTargetUsername().isBlank() || req.getCreatorId() == null) {
                return ResponseEntity.badRequest().build();
            }
            created = conversationService.createPrivateConversation(req.getCreatorId(), req.getTargetUsername(), req.getName());
        } else { // GROUP
            if (req.getCreatorId() == null || (req.getParticipantUsernames() == null || req.getParticipantUsernames().isEmpty())) {
                return ResponseEntity.badRequest().build();
            }
            created = conversationService.createGroupConversation(req.getCreatorId(), req.getParticipantUsernames(), req.getName());
        }

        ConversationDTO dto = conversationService.toDTO(created);
        return ResponseEntity.ok(dto);
    }
}
