package messagerie.application.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    private String displayName;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;

    public UserEntity(){}

    public UserEntity(String username,String displayName ,String email,  String passwordHash, LocalDateTime createdAt){
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    // Getters:

    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters:
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
