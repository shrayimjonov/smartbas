/*
package uz.davrbank.educontrolservice.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import uz.davrbank.educontrolservice.aggregate.dto.auth.LoginRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.RefreshTokenRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.Token;
import uz.davrbank.educontrolservice.aggregate.dto.auth.UserInfoResponse;
import uz.davrbank.educontrolservice.exception.condition.ServiceCommunicationException;
import uz.davrbank.educontrolservice.exception.condition.UnauthorizedException;
import uz.davrbank.educontrolservice.service.cases.employee.EmployeeService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

*/
/**
 * Unit tests for {@link KeycloakAuthService}.
 * Tests authentication, token refresh, and userinfo functionality.
 *//*

@ExtendWith(MockitoExtension.class)
@DisplayName("KeycloakAuthService Tests")
class KeycloakAuthServiceTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private RestTemplate restTemplate;

    private KeycloakAuthService keycloakAuthService;

    private static final String TOKEN_URL = "http://keycloak:8080/realms/edusystem/protocol/openid-connect/token";
    private static final String ISSUER_URI = "http://keycloak:8080/realms/edusystem";
    private static final String CLIENT_SECRET = "";
    private static final String CLIENT_ID = "inedu";
    private static final String SCOPE = "openid";

    @BeforeEach
    void setUp() {
        keycloakAuthService = new KeycloakAuthService(employeeService);
        
        // Inject mock RestTemplate
        ReflectionTestUtils.setField(keycloakAuthService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(keycloakAuthService, "tokenUrl", TOKEN_URL);
        ReflectionTestUtils.setField(keycloakAuthService, "issuerUri", ISSUER_URI);
        ReflectionTestUtils.setField(keycloakAuthService, "clientSecret", CLIENT_SECRET);
        ReflectionTestUtils.setField(keycloakAuthService, "clientId", CLIENT_ID);
        ReflectionTestUtils.setField(keycloakAuthService, "scope", SCOPE);
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should successfully login and return token with is_department flag")
        void login_Success_ReturnsTokenWithDepartmentFlag() {
            // Given
            LoginRequest loginRequest = new LoginRequest("testuser", "password123");
            Token expectedToken = createMockToken();
            
            when(restTemplate.postForEntity(eq(TOKEN_URL), any(HttpEntity.class), eq(Token.class)))
                    .thenReturn(ResponseEntity.ok(expectedToken));
            when(employeeService.checkEmployeeInDepartment("testuser")).thenReturn(true);

            // When
            Token result = keycloakAuthService.login(loginRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getAccessToken()).isEqualTo("mock-access-token");
            assertThat(result.getRefreshToken()).isEqualTo("mock-refresh-token");
            assertThat(result.isDepartment()).isTrue();
            
            verify(employeeService).checkEmployeeInDepartment("testuser");
        }

        @Test
        @DisplayName("Should throw UnauthorizedException when credentials are invalid")
        void login_InvalidCredentials_ThrowsUnauthorizedException() {
            // Given
            LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");
            
            when(restTemplate.postForEntity(eq(TOKEN_URL), any(HttpEntity.class), eq(Token.class)))
                    .thenThrow(HttpClientErrorException.Unauthorized.create(
                            HttpStatus.UNAUTHORIZED, "Unauthorized", HttpHeaders.EMPTY, null, null));

            // When/Then
            assertThatThrownBy(() -> keycloakAuthService.login(loginRequest))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("Неверные учётные данные");
        }

        @Test
        @DisplayName("Should throw ServiceCommunicationException when Keycloak is unavailable")
        void login_KeycloakUnavailable_ThrowsServiceCommunicationException() {
            // Given
            LoginRequest loginRequest = new LoginRequest("testuser", "password123");
            
            when(restTemplate.postForEntity(eq(TOKEN_URL), any(HttpEntity.class), eq(Token.class)))
                    .thenThrow(new ResourceAccessException("Connection refused"));

            // When/Then
            assertThatThrownBy(() -> keycloakAuthService.login(loginRequest))
                    .isInstanceOf(ServiceCommunicationException.class)
                    .hasMessageContaining("Не удалось подключиться к сервису аутентификации");
        }

        @Test
        @DisplayName("Should set is_department to false when employee not in department")
        void login_EmployeeNotInDepartment_SetsDepartmentFalse() {
            // Given
            LoginRequest loginRequest = new LoginRequest("testuser", "password123");
            Token expectedToken = createMockToken();
            
            when(restTemplate.postForEntity(eq(TOKEN_URL), any(HttpEntity.class), eq(Token.class)))
                    .thenReturn(ResponseEntity.ok(expectedToken));
            when(employeeService.checkEmployeeInDepartment("testuser")).thenReturn(false);

            // When
            Token result = keycloakAuthService.login(loginRequest);

            // Then
            assertThat(result.isDepartment()).isFalse();
        }
    }

    @Nested
    @DisplayName("Refresh Token Tests")
    class RefreshTokenTests {

        @Test
        @DisplayName("Should successfully refresh token")
        void refreshToken_Success_ReturnsNewToken() {
            // Given
            RefreshTokenRequest refreshRequest = new RefreshTokenRequest("old-refresh-token");
            Token expectedToken = createMockToken();
            
            when(restTemplate.postForEntity(eq(TOKEN_URL), any(HttpEntity.class), eq(Token.class)))
                    .thenReturn(ResponseEntity.ok(expectedToken));

            // When
            Token result = keycloakAuthService.refreshToken(refreshRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getAccessToken()).isEqualTo("mock-access-token");
        }

        @Test
        @DisplayName("Should throw UnauthorizedException when refresh token is expired")
        void refreshToken_ExpiredToken_ThrowsUnauthorizedException() {
            // Given
            RefreshTokenRequest refreshRequest = new RefreshTokenRequest("expired-refresh-token");

            when(restTemplate.postForEntity(eq(TOKEN_URL), any(HttpEntity.class), eq(Token.class)))
                    .thenThrow(HttpClientErrorException.BadRequest.create(
                            HttpStatus.BAD_REQUEST, "Token expired", HttpHeaders.EMPTY, null, null));

            // When/Then
            assertThatThrownBy(() -> keycloakAuthService.refreshToken(refreshRequest))
                    .isInstanceOf(UnauthorizedException.class);
        }
    }

    @Nested
    @DisplayName("Get User Info Tests")
    class GetUserInfoTests {

        private static final String USERINFO_URL = ISSUER_URI + "/protocol/openid-connect/userinfo";

        @Test
        @DisplayName("Should successfully get user info with Bearer token")
        void getUserInfo_Success_ReturnsUserInfo() {
            // Given
            String bearerToken = "Bearer valid-access-token";
            UserInfoResponse expectedUserInfo = createMockUserInfo();

            when(restTemplate.exchange(
                    eq(USERINFO_URL),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(UserInfoResponse.class)))
                    .thenReturn(ResponseEntity.ok(expectedUserInfo));

            // When
            UserInfoResponse result = keycloakAuthService.getUserInfo(bearerToken);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getPreferredUsername()).isEqualTo("testuser");
            assertThat(result.getEmail()).isEqualTo("testuser@example.com");
        }

        @Test
        @DisplayName("Should handle token without Bearer prefix")
        void getUserInfo_TokenWithoutBearerPrefix_Success() {
            // Given
            String token = "";
            UserInfoResponse expectedUserInfo = createMockUserInfo();

            when(restTemplate.exchange(
                    eq(USERINFO_URL),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(UserInfoResponse.class)))
                    .thenReturn(ResponseEntity.ok(expectedUserInfo));

            // When
            UserInfoResponse result = keycloakAuthService.getUserInfo(token);

            // Then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should throw UnauthorizedException when token is invalid")
        void getUserInfo_InvalidToken_ThrowsUnauthorizedException() {
            // Given
            String bearerToken = "Bearer invalid-token";

            when(restTemplate.exchange(
                    eq(USERINFO_URL),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(UserInfoResponse.class)))
                    .thenThrow(HttpClientErrorException.Unauthorized.create(
                            HttpStatus.UNAUTHORIZED, "Invalid token", HttpHeaders.EMPTY, null, null));

            // When/Then
            assertThatThrownBy(() -> keycloakAuthService.getUserInfo(bearerToken))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("Токен недействителен или истёк");
        }

        @Test
        @DisplayName("Should throw ServiceCommunicationException when Keycloak is unavailable")
        void getUserInfo_KeycloakUnavailable_ThrowsServiceCommunicationException() {
            // Given
            String bearerToken = "Bearer valid-token";

            when(restTemplate.exchange(
                    eq(USERINFO_URL),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(UserInfoResponse.class)))
                    .thenThrow(new ResourceAccessException("Connection refused"));

            // When/Then
            assertThatThrownBy(() -> keycloakAuthService.getUserInfo(bearerToken))
                    .isInstanceOf(ServiceCommunicationException.class)
                    .hasMessageContaining("Сервис аутентификации временно недоступен");
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
