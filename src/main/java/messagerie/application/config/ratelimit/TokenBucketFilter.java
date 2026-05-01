package messagerie.application.config.ratelimit;

import java.io.IOException;
import java.time.Duration;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import messagerie.application.dto.ErrorResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;

/**
 * Rate limit filter using a token-bucket per key. Key is the authenticated username when available,
 * otherwise the remote IP address. Bucket state is stored in Redis using Bucket4j ProxyManager.
 */
public class TokenBucketFilter extends OncePerRequestFilter {

    private final ProxyManager<byte[]> proxyManager;

    // Token bucket parameters: allow 10 tokens per second, burst capacity 20.
    private final BucketConfiguration bucketConfiguration = BucketConfiguration.builder()
            .addLimit(Bandwidth.classic(20, Refill.greedy(10, Duration.ofSeconds(1))))
            .build();

    public TokenBucketFilter(ProxyManager<byte[]> proxyManager) {
        this.proxyManager = proxyManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String key = resolveKey(request);

        // A deterministic Redis key per user/ip; proxy bucket state is shared across app instances.
        byte[] redisKey = ("rate-limit:" + key).getBytes(StandardCharsets.UTF_8);
        Bucket bucket = proxyManager.builder().build(redisKey, () -> bucketConfiguration);

        boolean consumed = bucket.tryConsume(1);
        if (!consumed) {
            // Too many requests
            response.setStatus(429);
            response.setHeader("Retry-After", "1");
            response.setContentType("application/json");
            ErrorResponse er = new ErrorResponse();
            er.setTimestamp(java.time.Instant.now());
            er.setStatus(429);
            er.setError("Too Many Requests");
            er.setMessage("Too Many Requests");
            er.setPath(request.getRequestURI());
            ObjectMapper om = new ObjectMapper();
            om.writeValue(response.getWriter(), er);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String resolveKey(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != null) {
            return "user:" + authentication.getName();
        }
        String ip = request.getRemoteAddr();
        return "ip:" + (ip == null ? "unknown" : ip);
    }
}



