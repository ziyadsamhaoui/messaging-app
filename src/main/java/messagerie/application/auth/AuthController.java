package messagerie.application.auth;

import messagerie.application.config.JwtService;
import messagerie.application.config.JwtRevocationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import messagerie.application.dto.AuthResponse;
import messagerie.application.dto.LoginRequest;
import messagerie.application.dto.RegisterRequest;
import messagerie.application.entity.UserEntity;
import messagerie.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping({"/auth", "/api/auth"})
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtRevocationService jwtRevocationService;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, JwtRevocationService jwtRevocationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.jwtRevocationService = jwtRevocationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            // keep consistent JSON error responses across the API
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new messagerie.application.dto.ErrorResponse(HttpStatus.CONFLICT.value(), "Conflict", "Username already exists", "/register", "username_conflict"));
        }

        UserEntity user = new UserEntity();
        user.setUsername(req.getUsername());
        user.setDisplayName(req.getDisplayName());
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        UserEntity saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getUsername(), saved.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, saved.getUserId(), saved.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        if (!auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new messagerie.application.dto.ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Invalid username or password", "/login", "auth_failed"));
        }

        UserEntity user = userRepository.findByUsername(req.getUsername()).orElseThrow(() -> new messagerie.application.exception.NotFoundException("User not found"));
        String token = jwtService.generateToken(user.getUsername(), user.getUserId());
        return ResponseEntity.ok(new AuthResponse(token, user.getUserId(), user.getUsername()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing Authorization header");
        }
        String token = authHeader.substring(7);
        try {
            Jws<Claims> parsed = jwtService.parseToken(token);
            Claims claims = parsed.getBody();
            String jti = claims.getId();
            if (jti != null) {
                long ttl = (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000L;
                if (ttl > 0) jwtRevocationService.revoke(jti, ttl);
            }
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
}

