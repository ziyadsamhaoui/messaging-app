package messagerie.application.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.UUID;
import messagerie.application.config.JwtRevocationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Simple JWT service to create and validate tokens.
 */
@Service
public class JwtService {

    private final Key signingKey;
    private final long expirationMs;
    private final JwtRevocationService revocationService;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expirationMs,
                      JwtRevocationService revocationService) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        this.revocationService = revocationService;
    }

    public String generateToken(String username, Long userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        String jti = UUID.randomUUID().toString();
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setId(jti)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }

    public boolean isTokenValid(String token, String username) {
        try {
            Claims claims = parseToken(token).getBody();
            String tokenUsername = claims.getSubject();
            Date expiration = claims.getExpiration();
            if (tokenUsername == null || !tokenUsername.equals(username) || !expiration.after(new Date())) {
                return false;
            }
            String jti = claims.getId();
            try {
                if (jti != null && revocationService.isRevoked(jti)) {
                    return false;
                }
            } catch (Exception ex) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

