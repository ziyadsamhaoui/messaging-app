package messagerie.application.config.ratelimit;

import java.io.IOException;
import java.time.Instant;
import com.fasterxml.jackson.databind.ObjectMapper;
import messagerie.application.dto.ErrorResponse;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SlidingWindowAuthFilter extends OncePerRequestFilter {

    // keep a deque of attempt timestamps (milliseconds) per key
    private final Map<String, Deque<Long>> attemptsCache;

    // window size (ms) and max attempts in that window
    private final long windowMillis;
    private final int maxAttempts;

    public SlidingWindowAuthFilter() {
        this.windowMillis = TimeUnit.MINUTES.toMillis(1); // 1 minute window
        this.maxAttempts = 5; // allow 5 attempts per window
        this.attemptsCache = new ConcurrentHashMap<>();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // Only filter auth/login/register endpoints
        return !(path.equals("/login") || path.equals("/register") || path.startsWith("/auth/"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        String username = extractUsernameFromParam(request);

        boolean blocked = checkAndRecord("ip:" + ip);
        if (username != null && !username.isBlank()) {
            blocked = blocked || checkAndRecord("user:" + username);
        }

        if (blocked) {
            response.setStatus(429);
            response.setHeader("Retry-After", "60");
            response.setContentType("application/json");
            ErrorResponse er = new ErrorResponse();
            er.setTimestamp(Instant.now());
            er.setStatus(429);
            er.setError("Too Many Requests");
            er.setMessage("Too many authentication attempts, try again later");
            er.setPath(request.getRequestURI());
            ObjectMapper om = new ObjectMapper();
            om.writeValue(response.getWriter(), er);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean checkAndRecord(String key) {
        long now = Instant.now().toEpochMilli();
        Deque<Long> deque = attemptsCache.computeIfAbsent(key, k -> new ArrayDeque<>());
        synchronized (deque) {
            // remove old timestamps
            while (!deque.isEmpty() && deque.peekFirst() < now - windowMillis) {
                deque.pollFirst();
            }
            if (deque.size() >= maxAttempts) {
                return true;
            }
            deque.addLast(now);
            return false;
        }
    }

    private String extractUsernameFromParam(HttpServletRequest request) {
        // try common parameter names without reading body
        String u = request.getParameter("username");
        if (u != null) return u;
        u = request.getParameter("user");
        if (u != null) return u;
        u = request.getParameter("email");
        if (u != null) return u;
        return null;
    }
}




