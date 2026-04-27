package messagerie.application.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.stereotype.Service;

@Service
public class JwtRevocationService {

    private final RedisClient redisClient;

    public JwtRevocationService(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    public void revoke(String jti, long ttlSeconds) {
        String key = "jwt:blacklist:" + jti;
        try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
            RedisCommands<String, String> sync = conn.sync();
            sync.setex(key, ttlSeconds, "1");
        }
    }

    public boolean isRevoked(String jti) {
        if (jti == null) return false;
        String key = "jwt:blacklist:" + jti;
        try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
            RedisCommands<String, String> sync = conn.sync();
            String v = sync.get(key);
            return v != null;
        }
    }
}


