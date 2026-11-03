package uz.davrbank.educontrolservice.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.auth.LoginRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.RefreshTokenRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.Token;
import uz.davrbank.educontrolservice.aggregate.dto.auth.UserInfoResponse;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.auth.KeycloakAuthService;
import uz.davrbank.educontrolservice.service.dao.security.UserInterface;

import java.util.List;

/**
 * REST контроллер для аутентификации через API Gateway.
 * Проксирует запросы к Keycloak, скрывая client_secret от фронтенда.
 *
 * <p>Эндпоинты:</p>
 * <ul>
 *   <li>POST /api/v1/educontrol/auth/login - аутентификация по логину/паролю</li>
 *   <li>POST /api/v1/educontrol/auth/refresh - обновление токена</li>
 *   <li>GET /api/v1/educontrol/auth/userinfo - получение информации о пользователе</li>
 *   <li>POST /api/v1/educontrol/auth/logout - выход из системы</li>
 * </ul>
 *
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/educontrol/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;
    private final UserInterface userInterface;

    /**
     * Аутентификация пользователя по логину и паролю.
     * Client credentials (client_id, client_secret) добавляются на стороне сервера.
     *
     * @param loginRequest запрос с логином и паролем
     * @return токен доступа с дополнительной информацией (is_department)
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Token>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Запрос на аутентификацию пользователя: {}", loginRequest.getUsername());
        Token token = keycloakAuthService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.of(token));
    }

    /**
     * Обновление токена доступа с помощью refresh_token.
     * Client credentials добавляются на стороне сервера.
     *
     * @param refreshRequest запрос с refresh_token
     * @return новый токен доступа
     */
    @PostMapping("/refresh")
    public ResponseEntity<Token> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshRequest) {
        log.debug("Запрос на обновление токена");
        Token token = keycloakAuthService.refreshToken(refreshRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * Получение информации о текущем пользователе.
     * Требует валидный Bearer токен в заголовке Authorization.
     *
     * @param authorization заголовок Authorization с Bearer токеном
     * @return информация о пользователе из Keycloak
     */
    @GetMapping("/userinfo")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        "Информация о пользователе получена", keycloakAuthService.getUserInfo(authorization))
        );
    }

    /**
     * Proper Secure Logout.
     * Invalidate the session on Keycloak side to prevent token reuse.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequest request) {
        log.info("Request to logout user and revoke refresh token");

        // Call Keycloak to invalidate the session
        keycloakAuthService.logout(request.getRefreshToken());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<List<Object>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.of("Список пользователей получен", userInterface.getAllUsers()));
    }

}

