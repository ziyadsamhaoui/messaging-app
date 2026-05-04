package messagerie.application.controller;

import messagerie.application.dto.UserDTO;
import messagerie.application.entity.UserEntity;
import messagerie.application.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/me")
	public ResponseEntity<UserDTO> me() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
			return ResponseEntity.status(401).build();
		}
		return userRepository.findByUsername(auth.getName())
				.map(u -> ResponseEntity.ok(toDto(u)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
		return userRepository.findById(id)
				.map(u -> ResponseEntity.ok(toDto(u)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/by-username/{username}")
	public ResponseEntity<UserDTO> getByUsername(@PathVariable String username) {
		return userRepository.findByUsername(username)
				.map(u -> ResponseEntity.ok(toDto(u)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<UserDTO>> search(@RequestParam(required = false, name = "q") String q) {
		if (q == null || q.isBlank()) {
			List<UserDTO> all = userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
			return ResponseEntity.ok(all);
		}
		List<UserDTO> results = userRepository.findAll().stream()
				.filter(u -> u.getUsername().contains(q) || (u.getDisplayName() != null && u.getDisplayName().contains(q)))
				.map(this::toDto)
				.collect(Collectors.toList());
		return ResponseEntity.ok(results);
	}

	private UserDTO toDto(UserEntity u) {
		return new UserDTO(u.getUserId(), u.getUsername(), u.getDisplayName(), u.getEmail(), u.getCreatedAt());
	}
}
