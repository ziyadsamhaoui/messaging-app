package messagerie.application.controller;

import messagerie.application.dto.ConversationDTO;
import messagerie.application.dto.ConversationPageDTO;
import messagerie.application.dto.CreateConversationRequest;
import messagerie.application.entity.ConversationEntity;
import messagerie.application.service.ConversationService;
import messagerie.application.repository.UserRepository;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final UserRepository userRepository;

    public ConversationController(ConversationService conversationService, UserRepository userRepository) {
        this.conversationService = conversationService;
        this.userRepository = userRepository;
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
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody CreateConversationRequest req, Principal principal) {
        if (req == null || req.getType() == null) {
            return ResponseEntity.badRequest().build();
        }

        ConversationEntity created;
        // Derive creatorId from authenticated principal (ignore any client-supplied creatorId)
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        var optUser = userRepository.findByUsername(principal.getName());
        if (optUser.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        Long creatorId = optUser.get().getUserId();

        if (req.getType() == messagerie.application.enums.ConversationType.PRIVATE) {
            if (req.getTargetUsername() == null || req.getTargetUsername().isBlank()) {
                return ResponseEntity.badRequest().build();
            }
            created = conversationService.createPrivateConversation(creatorId, req.getTargetUsername(), req.getName());
        } else { // GROUP
            if (req.getParticipantUsernames() == null || req.getParticipantUsernames().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Enforce maximum group size to avoid abuse
            if (req.getParticipantUsernames().size() > 50) {
                return ResponseEntity.status(400).body(null);
            }

            created = conversationService.createGroupConversation(creatorId, req.getParticipantUsernames(), req.getName());
        }

        ConversationDTO dto = conversationService.toDTO(created);
        return ResponseEntity.ok(dto);
    }
}
