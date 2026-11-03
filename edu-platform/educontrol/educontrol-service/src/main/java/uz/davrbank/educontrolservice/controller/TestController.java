package uz.davrbank.educontrolservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;

import java.util.List;

//@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DAVR', 'ROLE_HR')")
@RestController
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);
    private final JwtTokenService jwtTokenService;

    public TestController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping("/api/v1/test")
    public ResponseEntity<ApiResponse<List<String>>> testKEY() {
        log.info("testKEY");
        List<String> roles = jwtTokenService.roles();
        log.info("roles: {}", roles);
        return ResponseEntity.ok(ApiResponse.of("Тест успешно выполнен", roles));
    }


    @PreAuthorize("hasRole('DEPARTMENT')")
    @GetMapping("/api/v1/test/up")
    public ResponseEntity<ApiResponse<Void>> testKEYCloak() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(auth -> log.info("Authority: {}", auth.getAuthority()));
        return ResponseEntity.ok(ApiResponse.of("Тест Keycloak успешно выполнен"));
    }


    @GetMapping("/api/v1/debug")
    public ResponseEntity<ApiResponse<Void>> debugToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            log.debug("JWT Claims: {}", jwt.getClaims());
        }
        return ResponseEntity.ok(ApiResponse.of("Проверьте логи для просмотра JWT claims"));
    }


}
