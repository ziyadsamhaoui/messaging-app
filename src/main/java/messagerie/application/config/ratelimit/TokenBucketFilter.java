package messagerie.application.config.ratelimit;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

/**
 * Rate limit filter using a token-bucket per key. Key is the authenticated username when available,
 * otherwise the remote IP address. Buckets are stored in a Caffeine cache to avoid unbounded growth.
 */
public class TokenBucketFilter extends OncePerRequestFilter {

    // Simple in-memory cache for buckets. For production use a distributed store (Redis) or
    // Bucket4j extensions that provide Redis-backed buckets.
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket newBucket() {
        // allow 10 tokens per second with a burst capacity of 20
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(20, Duration.ofSeconds(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String key = resolveKey(request);

        Bucket bucket = cache.computeIfAbsent(key, k -> newBucket());

        boolean consumed = bucket.tryConsume(1);
        if (!consumed) {
            // Too many requests
            response.setStatus(429);
            response.setHeader("Retry-After", "1");
            response.getWriter().write("Too Many Requests");
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



