package messagerie.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import messagerie.application.config.ratelimit.SlidingWindowAuthFilter;
import messagerie.application.config.ratelimit.TokenBucketFilter;
import messagerie.application.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtService jwtService,
                                                   CustomUserDetailsService customUserDetailsService,
                                                   TokenBucketFilter tokenBucketFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/ws", "/ws/**", "/app/**", "/topic/**", "/queue/**").permitAll()
                .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/auth/**", "/login", "/register", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            ;

        // Register JWT filter so that requests with a valid Bearer token are authenticated
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService, customUserDetailsService);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Sliding-window counter for authentication endpoints (IP + username where available)
        SlidingWindowAuthFilter authRateLimiter = new SlidingWindowAuthFilter();
        http.addFilterBefore(authRateLimiter, UsernamePasswordAuthenticationFilter.class);

        // Token-bucket per user for general rate limiting (applies after JWT authentication so we
        // can key by authenticated username). Unauthenticated requests will be keyed by IP.
        http.addFilterAfter(tokenBucketFilter, JwtAuthenticationFilter.class);

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

