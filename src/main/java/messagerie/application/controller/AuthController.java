package messagerie.application.controller;

import messagerie.application.config.JwtService;
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
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity user = userRepository.findByUsername(req.getUsername()).get();
        String token = jwtService.generateToken(user.getUsername(), user.getUserId());
        return ResponseEntity.ok(new AuthResponse(token, user.getUserId(), user.getUsername()));
    }
}

