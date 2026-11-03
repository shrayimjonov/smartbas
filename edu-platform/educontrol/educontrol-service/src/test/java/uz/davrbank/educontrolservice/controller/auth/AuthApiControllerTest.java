package uz.davrbank.educontrolservice.controller.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uz.davrbank.educontrolservice.aggregate.dto.auth.LoginRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.RefreshTokenRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.Token;
import uz.davrbank.educontrolservice.aggregate.dto.auth.UserInfoResponse;
import uz.davrbank.educontrolservice.exception.dto.ErrorCode;
import uz.davrbank.educontrolservice.exception.condition.UnauthorizedException;
import uz.davrbank.educontrolservice.service.auth.KeycloakAuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuthController}.
 * Tests REST endpoints for authentication.
 */
/*
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthApiController Tests")
class AuthApiControllerTest {

    @Mock
    private KeycloakAuthService keycloakAuthService;

    @InjectMocks
    private AuthController authApiController;

    @Nested
    @DisplayName("POST /api/auth/login")
    class LoginEndpointTests {

        @Test
        @DisplayName("Should return 200 and token on successful login")
        void login_ValidCredentials_ReturnsToken() {
            // Given
            LoginRequest loginRequest = new LoginRequest("testuser", "password123");
            Token expectedToken = createMockToken();

            when(keycloakAuthService.login(any(LoginRequest.class))).thenReturn(expectedToken);

            // When
            ResponseEntity<Token> response = authApiController.login(loginRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getAccessToken()).isEqualTo("mock-access-token");
            assertThat(response.getBody().getRefreshToken()).isEqualTo("mock-refresh-token");
            assertThat(response.getBody().isDepartment()).isTrue();
        }

        @Test
        @DisplayName("Should throw UnauthorizedException on invalid credentials")
        void login_InvalidCredentials_ThrowsUnauthorizedException() {
            // Given
            LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

            when(keycloakAuthService.login(any(LoginRequest.class)))
                    .thenThrow(new UnauthorizedException(ErrorCode.UNAUTHORIZED, "Неверные учётные данные"));

            // When/Then
            assertThatThrownBy(() -> authApiController.login(loginRequest))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("Неверные учётные данные");
        }
    }

    @Nested
    @DisplayName("POST /api/auth/refresh")
    class RefreshEndpointTests {

        @Test
        @DisplayName("Should return 200 and new token on successful refresh")
        void refresh_ValidToken_ReturnsNewToken() {
            // Given
            RefreshTokenRequest refreshRequest = new RefreshTokenRequest("valid-refresh-token");
            Token expectedToken = createMockToken();

            when(keycloakAuthService.refreshToken(any(RefreshTokenRequest.class)))
                    .thenReturn(expectedToken);

            // When
            ResponseEntity<Token> response = authApiController.refreshToken(refreshRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getAccessToken()).isEqualTo("mock-access-token");
        }

        @Test
        @DisplayName("Should throw UnauthorizedException when refresh token is expired")
        void refresh_ExpiredToken_ThrowsUnauthorizedException() {
            // Given
            RefreshTokenRequest refreshRequest = new RefreshTokenRequest("expired-refresh-token");

            when(keycloakAuthService.refreshToken(any(RefreshTokenRequest.class)))
                    .thenThrow(new UnauthorizedException(ErrorCode.UNAUTHORIZED, "Refresh token истёк"));

            // When/Then
            assertThatThrownBy(() -> authApiController.refreshToken(refreshRequest))
                    .isInstanceOf(UnauthorizedException.class);
        }
    }

    @Nested
    @DisplayName("GET /api/auth/userinfo")
    class UserInfoEndpointTests {

        @Test
        @DisplayName("Should return 200 and user info with valid token")
        void userInfo_ValidToken_ReturnsUserInfo() {
            // Given
            String bearerToken = "Bearer valid-access-token";
            UserInfoResponse expectedUserInfo = createMockUserInfo();

            when(keycloakAuthService.getUserInfo(anyString())).thenReturn(expectedUserInfo);

            // When
            ResponseEntity<UserInfoResponse> response = authApiController.getUserInfo(bearerToken);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getPreferredUsername()).isEqualTo("testuser");
            assertThat(response.getBody().getEmail()).isEqualTo("testuser@example.com");
        }

        @Test
        @DisplayName("Should throw UnauthorizedException when token is invalid")
        void userInfo_InvalidToken_ThrowsUnauthorizedException() {
            // Given
            String bearerToken = "Bearer invalid-token";

            when(keycloakAuthService.getUserInfo(anyString()))
                    .thenThrow(new UnauthorizedException(ErrorCode.TOKEN_INVALID, "Токен недействителен"));

            // When/Then
            assertThatThrownBy(() -> authApiController.getUserInfo(bearerToken))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("Токен недействителен");
        }
    }

    @Nested
    @DisplayName("POST /api/auth/logout")
    class LogoutEndpointTests {

        @Test
        @DisplayName("Should return 200 on logout")
        void logout_Success_Returns200() {
            // When
            ResponseEntity<Void> response = authApiController.logout();

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    // Helper methods
    private Token createMockToken() {
        Token token = new Token();
        token.setAccessToken("mock-access-token");
        token.setRefreshToken("mock-refresh-token");
        token.setExpiresIn(300L);
        token.setRefreshExpiresIn(1800L);
        token.setTokenType("Bearer");
        token.setScope("openid");
        token.setDepartment(true);
        return token;
    }

    private UserInfoResponse createMockUserInfo() {
        UserInfoResponse userInfo = new UserInfoResponse();
        userInfo.setSub("user-uuid-123");
        userInfo.setPreferredUsername("testuser");
        userInfo.setEmail("testuser@example.com");
        userInfo.setEmailVerified(true);
        userInfo.setName("Test User");
        userInfo.setGivenName("Test");
        userInfo.setFamilyName("User");
        return userInfo;
    }
}
*/

