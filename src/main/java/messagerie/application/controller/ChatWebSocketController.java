package messagerie.application.controller;

import lombok.RequiredArgsConstructor;
import messagerie.application.dto.ChatMessage;
import messagerie.application.dto.MessageDTO;
import messagerie.application.service.ConversationService;
import messagerie.application.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final ConversationService conversationService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        Long conversationId = chatMessage.getConversationId();

        // If no conversationId provided but a targetUsername is provided, create/find private conversation
        if (conversationId == null && chatMessage.getTargetUsername() != null && !chatMessage.getTargetUsername().isBlank()) {
            var conversation = conversationService.createPrivateConversation(chatMessage.getSenderId(), chatMessage.getTargetUsername());
            conversationId = conversation.getConversationId();
        }

        if (conversationId == null) {
            throw new RuntimeException("Conversation id is required if target username is not provided");
        }

        var saved = messageService.sendMessage(
                chatMessage.getSenderId(),
                conversationId,
                chatMessage.getContent()
        );

        MessageDTO dto = new MessageDTO(
                saved.getMessageId(),
                saved.getConversationId(),
                saved.getUserId(),
                saved.getContent(),
                saved.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId, dto);
    }
}
