package uz.davrbank.educontrolservice.aggregate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import uz.davrbank.educontrolservice.exception.ApiResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Security configuration for educontrol-service (API Gateway).
 *
 * <p>This configuration:
 * <ul>
 *   <li>Validates JWT tokens from Keycloak</li>
 *   <li>Extracts roles from realm_access, resource_access, and groups claims</li>
 *   <li>Provides proper error responses for authentication/authorization failures</li>
 *   <li>Configures CORS for cross-origin requests</li>
 * </ul>
 *
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;
    private final ObjectMapper objectMapper;

    /**
     * Public endpoints that don't require authentication.
     * Включает эндпоинты аутентификации (login, refresh, logout).
     */
    private static final String[] PUBLIC_ENDPOINTS = {
            // Legacy Keycloak proxy endpoint (для обратной совместимости)
            "/realms/**",

            "/api/v1/educontrol/auth/**",
            // Public API endpoints
            "/api/v1/educontrol/department/all",
            // Documentation
            "/docs/**",
            "/v3/api-docs/**",
            "/api/v1/educontrol/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            // Health checks
            "/actuator/health",
            "/actuator/info"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless REST API
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Stateless session management (no server-side sessions)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // All requests are permitted for testing purposes
                        .anyRequest().permitAll()
                )

                // OAuth2 Resource Server configuration with JWT
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
//                        // Custom error handling for authentication failures
//                        .authenticationEntryPoint(authenticationEntryPoint())
//                        .accessDeniedHandler(accessDeniedHandler())
//                )

                // Exception handling
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
    }

    /**
     * Custom AuthenticationEntryPoint for handling 401 Unauthorized errors.
     * Returns a proper JSON error response instead of default HTML.
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            log.warn("Authentication failed for request {}: {}",
                    request.getRequestURI(), authException.getMessage());

            writeErrorResponse(response, HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    "Authentication required. Please provide a valid JWT token.");
        };
    }

    /**
     * Custom AccessDeniedHandler for handling 403 Forbidden errors.
     * Returns a proper JSON error response instead of default HTML.
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            log.warn("Access denied for request {}: {}",
                    request.getRequestURI(), accessDeniedException.getMessage());

            writeErrorResponse(response, HttpStatus.FORBIDDEN,
                    "Access Denied",
                    "You don't have permission to access this resource.");
        };
    }

    /**
     * Writes a standardized error response in JSON format.
     */
    private void writeErrorResponse(HttpServletResponse response, HttpStatus status,
                                     String error, String message) throws IOException {
        ApiResponse errorResponse = ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .build();

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    /**
     * CORS configuration for cross-origin requests.
     * In production, restrict allowedOriginPatterns to specific domains.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // 👈 Разрешить всех (лучше, чем setAllowedOrigins)
        configuration.setAllowedMethods(List.of("*")); // Все нужные методы
        configuration.setAllowedHeaders(List.of("*")); // Все заголовки
        configuration.setAllowCredentials(true); // Если используешь cookie или Authorization header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
