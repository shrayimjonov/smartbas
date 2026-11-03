package uz.davrbank.educontrolservice.service.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import uz.davrbank.educontrolservice.aggregate.dto.auth.LoginRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.RefreshTokenRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.Token;
import uz.davrbank.educontrolservice.aggregate.dto.auth.UserInfoResponse;
import uz.davrbank.educontrolservice.exception.condition.BusinessValidationException;
import uz.davrbank.educontrolservice.exception.condition.ServiceCommunicationException;
import uz.davrbank.educontrolservice.exception.condition.UnauthorizedException;
import uz.davrbank.educontrolservice.exception.dto.ErrorCode;
import uz.davrbank.educontrolservice.service.cases.employee.EmployeeService;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;

/**
 * Сервис для взаимодействия с Keycloak для аутентификации и авторизации.
 * Централизует всю логику работы с Keycloak, скрывая client_secret от фронтенда.
 *
 * <p>Поддерживаемые операции:</p>
 * <ul>
 *   <li>Аутентификация по логину/паролю (grant_type=password)</li>
 *   <li>Обновление токена (grant_type=refresh_token)</li>
 *   <li>Получение информации о пользователе (userinfo endpoint)</li>
 * </ul>
 *
 * @version 1.0.0
 */
@Service
@Slf4j
public class KeycloakAuthService {

    private final EmployeeService employeeService;
    private final RestTemplate restTemplate;
    private final JwtTokenService jwtTokenService;

    @Value("${keycloak.token.url}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${token.client.secret}")
    private String clientSecret;

    @Value("${token.client.id}")
    private String clientId;

    @Value("${token.client.grantType}")
    private String grantType;

    @Value("${token.client.scope}")
    private String scope;

    public KeycloakAuthService(EmployeeService employeeService, JwtTokenService jwtTokenService) {
        this.employeeService = employeeService;
        this.jwtTokenService = jwtTokenService;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Аутентификация пользователя по логину и паролю.
     * Добавляет client_secret на стороне сервера и обогащает ответ флагом is_department.
     *
     * @param loginRequest запрос с логином и паролем
     * @return токен доступа с дополнительной информацией
     * @throws UnauthorizedException если учётные данные неверны
     * @throws ServiceCommunicationException если Keycloak недоступен
     */
    public Token login(LoginRequest loginRequest) {
        log.info("Попытка аутентификации пользователя: {}", loginRequest.getUsername());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", loginRequest.getUsername());
        form.add("password", loginRequest.getPassword());
        form.add("client_secret", clientSecret);
        form.add("client_id", clientId);
        form.add("grant_type", grantType);
        form.add("scope", scope);

        Token token = executeTokenRequest(form);

        // Обогащаем токен информацией о принадлежности к отделу
        boolean isInDepartment = employeeService.checkEmployeeInDepartment(loginRequest.getUsername());
        token.setDepartment(isInDepartment);

        log.info("Пользователь {} успешно аутентифицирован, is_department={}", 
                loginRequest.getUsername(), isInDepartment);

        return token;
    }

    /**
     * Обновление токена доступа с помощью refresh_token.
     * Добавляет client_secret на стороне сервера.
     *
     * @param refreshRequest запрос с refresh_token
     * @return новый токен доступа
     * @throws UnauthorizedException если refresh_token недействителен или истёк
     * @throws ServiceCommunicationException если Keycloak недоступен
     */
    public Token refreshToken(RefreshTokenRequest refreshRequest) {
        log.debug("Запрос на обновление токена");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("refresh_token", refreshRequest.getRefreshToken());
        form.add("client_secret", clientSecret);
        form.add("client_id", clientId);
        form.add("grant_type", "refresh_token");

        Token token = executeTokenRequest(form);

        log.debug("Токен успешно обновлён");
        return token;
    }

    /**
     * Получение информации о текущем пользователе из Keycloak.
     * Использует Bearer токен из заголовка Authorization.
     *
     * @param bearerToken токен доступа (с префиксом "Bearer " или без)
     * @return информация о пользователе
     * @throws UnauthorizedException если токен недействителен
     * @throws ServiceCommunicationException если Keycloak недоступен
     */
    public UserInfoResponse getUserInfo(String bearerToken) {
        log.debug("Запрос информации о пользователе");

        String userInfoUrl = issuerUri + "/protocol/openid-connect/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractToken(bearerToken));


        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserInfoResponse> response = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.POST,
                    request,
                    UserInfoResponse.class
            );

            if (response.getBody() == null) {
                throw new UnauthorizedException(ErrorCode.TOKEN_INVALID,
                        "Не удалось получить информацию о пользователе");
            }

            response.getBody().setDepartment(jwtTokenService.getDepartmentId() != null);
            response.getBody().setDepartmentId(jwtTokenService.getDepartmentId());
            log.debug("Информация о пользователе получена: {}", 
                    response.getBody().getPreferredUsername());
            return response.getBody();

        } catch (HttpClientErrorException.Unauthorized e) {
            log.warn("Недействительный токен при запросе userinfo: {}", e.getMessage());
            throw new UnauthorizedException(ErrorCode.TOKEN_INVALID, 
                    "Токен недействителен или истёк");
        } catch (HttpClientErrorException e) {
            log.error("Ошибка клиента при запросе userinfo: {} - {}", 
                    e.getStatusCode(), e.getMessage());
            throw new UnauthorizedException(ErrorCode.TOKEN_INVALID, 
                    "Ошибка при получении информации о пользователе");
        } catch (ResourceAccessException e) {
            log.error("Keycloak недоступен при запросе userinfo: {}", e.getMessage());
            throw new ServiceCommunicationException(ErrorCode.SERVICE_UNAVAILABLE,
                    "Сервис аутентификации временно недоступен");
        }
    }

    /**
     * Выполняет запрос к token endpoint Keycloak.
     *
     * @param form параметры запроса
     * @return токен от Keycloak
     */
    private Token executeTokenRequest(MultiValueMap<String, String> form) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        try {
            ResponseEntity<Token> response = restTemplate.postForEntity(tokenUrl, request, Token.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() == null) {
                throw new UnauthorizedException(ErrorCode.TOKEN_INVALID,
                        "Не удалось получить токен от сервера аутентификации");
            }
            return response.getBody();

        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.BadRequest e) {
            log.warn("Ошибка аутентификации: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED,
                    "Неверные учётные данные");
        } catch (HttpClientErrorException e) {
            log.error("Ошибка клиента при запросе токена: {} - {}",
                    e.getStatusCode(), e.getMessage());
            throw new BusinessValidationException(ErrorCode.BAD_REQUEST,
                    "Ошибка при обработке запроса аутентификации");
        } catch (HttpServerErrorException e) {
            log.error("Ошибка сервера Keycloak: {} - {}", e.getStatusCode(), e.getMessage());
            throw new ServiceCommunicationException(ErrorCode.SERVICE_UNAVAILABLE,
                    "Сервис аутентификации временно недоступен");
        } catch (ResourceAccessException e) {
            log.error("Keycloak недоступен: {}", e.getMessage());
            throw new ServiceCommunicationException(ErrorCode.SERVICE_UNAVAILABLE,
                    "Не удалось подключиться к сервису аутентификации");
        }
    }

    /**
     * Извлекает токен из строки Authorization header.
     * Удаляет префикс "Bearer " если присутствует.
     *
     * @param bearerToken строка с токеном
     * @return чистый токен без префикса
     */
    private String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    public void logout(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken); // This kills the session

        try {
            String logoutUrl = String.format("%s/protocol/openid-connect/logout", issuerUri);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

            restTemplate.postForEntity(logoutUrl, request, Void.class);

        } catch (Exception e) {
            log.warn("Logout failed or token already invalid: {}", e.getMessage());
        }
    }
}

