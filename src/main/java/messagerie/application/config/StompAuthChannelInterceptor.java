package messagerie.application.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import messagerie.application.repository.ConversationParticipantRepository;
import messagerie.application.repository.UserRepository;
import messagerie.application.service.CustomUserDetailsService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import java.time.Duration;
import java.nio.charset.StandardCharsets;

@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final ConversationParticipantRepository participantRepository;
    private final ProxyManager<byte[]> bucketProxyManager;
    private final BucketConfiguration bucketConfiguration = BucketConfiguration.builder()
            .addLimit(Bandwidth.classic(20, Refill.greedy(10, Duration.ofSeconds(1))))
            .build();

    public StompAuthChannelInterceptor(JwtService jwtService,
                                       CustomUserDetailsService customUserDetailsService,
                                       UserRepository userRepository,
                                       ConversationParticipantRepository participantRepository,
                                       ProxyManager<byte[]> bucketProxyManager) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.bucketProxyManager = bucketProxyManager;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();
        if (command == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(command)) {
            authenticateConnect(accessor);
        } else if (StompCommand.SEND.equals(command)) {
            enforceSendAuthorization(accessor);
        } else if (StompCommand.SUBSCRIBE.equals(command)) {
            enforceSubscribeAuthorization(accessor);
        }

        return message;
    }

    private void authenticateConnect(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            authHeader = accessor.getFirstNativeHeader("authorization");
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AccessDeniedException("Missing or invalid Authorization header for STOMP CONNECT");
        }

        String token = authHeader.substring(7);
        try {
            Jws<Claims> parsed = jwtService.parseToken(token);
            String username = parsed.getBody().getSubject();
            if (username == null || username.isBlank()) {
                throw new AccessDeniedException("Invalid token subject");
            }

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (!jwtService.isTokenValid(token, userDetails.getUsername())) {
                throw new AccessDeniedException("Invalid JWT token");
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            accessor.setUser(authentication);
        } catch (Exception ex) {
            throw new AccessDeniedException("STOMP authentication failed");
        }
    }

    private void enforceSendAuthorization(StompHeaderAccessor accessor) {
        if (accessor.getUser() == null) {
            throw new AccessDeniedException("Unauthenticated STOMP SEND");
        }

        String destination = accessor.getDestination();
        if (destination != null && destination.startsWith("/app/")) {
            // For app destinations, authenticated principal is mandatory.
            // Fine-grained checks by conversation id happen at service/controller level.
            // Apply per-user websocket rate limiter for messages sent to application destinations
            try {
                String username = accessor.getUser().getName();
                String key = "ws:rate:user:" + (username == null ? "anonymous" : username);
                byte[] redisKey = key.getBytes(StandardCharsets.UTF_8);
                Bucket bucket = bucketProxyManager.builder().build(redisKey, () -> bucketConfiguration);
                boolean consumed = bucket.tryConsume(1);
                if (!consumed) {
                    throw new AccessDeniedException("WebSocket rate limit exceeded");
                }
            } catch (AccessDeniedException ex) {
                throw ex;
            } catch (Exception ex) {
                // In case of any failure talking to Redis or bucket manager, deny to be safe
                throw new AccessDeniedException("WebSocket rate limiting failed");
            }
            return;
        }
    }

    private void enforceSubscribeAuthorization(StompHeaderAccessor accessor) {
        if (accessor.getUser() == null) {
            throw new AccessDeniedException("Unauthenticated STOMP SUBSCRIBE");
        }

        String destination = accessor.getDestination();
        if (destination == null) {
            return;
        }

        if (destination.startsWith("/topic/conversations/")) {
            Long conversationId = parseConversationId(destination);
            String username = accessor.getUser().getName();
            Long userId = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AccessDeniedException("User not found"))
                    .getUserId();

            boolean allowed = participantRepository.existsByConversationIdAndUserId(conversationId, userId);
            if (!allowed) {
                throw new AccessDeniedException("Not allowed to subscribe to this conversation");
            }
        }
    }

    private Long parseConversationId(String destination) {
        String idPart = destination.substring("/topic/conversations/".length());
        // Strip optional trailing path segments if any future extension is added.
        int slashIdx = idPart.indexOf('/');
        if (slashIdx >= 0) {
            idPart = idPart.substring(0, slashIdx);
        }
        try {
            return Long.parseLong(idPart);
        } catch (NumberFormatException ex) {
            throw new AccessDeniedException("Invalid conversation destination");
        }
    }
}

