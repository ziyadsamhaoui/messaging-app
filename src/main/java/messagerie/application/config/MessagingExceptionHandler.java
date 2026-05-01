package messagerie.application.config;

import messagerie.application.dto.ErrorResponse;
import messagerie.application.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class MessagingExceptionHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final Logger log = LoggerFactory.getLogger(MessagingExceptionHandler.class);

    public MessagingExceptionHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageExceptionHandler({BadRequestException.class, NotFoundException.class, ForbiddenException.class, ConflictException.class, RateLimitException.class})
    public void handleDomainException(Exception ex, SimpMessageHeaderAccessor headerAccessor) {
        String user = headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : null;
        String sessionId = headerAccessor.getSessionId();
        ErrorResponse er = new ErrorResponse();
        er.setTimestamp(Instant.now());
        if (ex instanceof RateLimitException) {
            er.setStatus(429);
            er.setError("Too Many Requests");
        } else if (ex instanceof BadRequestException) {
            er.setStatus(400);
            er.setError("Bad Request");
        } else if (ex instanceof NotFoundException) {
            er.setStatus(404);
            er.setError("Not Found");
        } else if (ex instanceof ForbiddenException) {
            er.setStatus(403);
            er.setError("Forbidden");
        } else if (ex instanceof ConflictException) {
            er.setStatus(409);
            er.setError("Conflict");
        } else {
            er.setStatus(500);
            er.setError("Internal Server Error");
        }
        er.setMessage(ex.getMessage());
        try {
            if (user != null) {
                messagingTemplate.convertAndSendToUser(user, "/queue/errors", er);
            } else if (sessionId != null) {
                messagingTemplate.convertAndSend("/queue/errors-" + sessionId, er);
            } else {
                log.warn("Could not send messaging error to user or session; error: {}", er.getMessage());
            }
        } catch (Exception sendEx) {
            log.error("Failed to send messaging error to user: {}", sendEx.getMessage(), sendEx);
        }
    }

    @MessageExceptionHandler(Exception.class)
    public void handleGeneric(Exception ex, SimpMessageHeaderAccessor headerAccessor) {
        log.error("Unhandled exception in messaging: ", ex);
        ErrorResponse er = new ErrorResponse();
        er.setTimestamp(Instant.now());
        er.setStatus(500);
        er.setError("Internal Server Error");
        er.setMessage("An internal error occurred");
        String user = headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : null;
        try {
            if (user != null) {
                messagingTemplate.convertAndSendToUser(user, "/queue/errors", er);
            }
        } catch (Exception sendEx) {
            log.error("Failed to send generic messaging error to user: {}", sendEx.getMessage(), sendEx);
        }
    }
}

