package uz.davrbank.educontrolservice.service.cases.security;

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
import uz.davrbank.educontrolservice.aggregate.dto.auth.LoginRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.RefreshTokenRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.Token;
import uz.davrbank.educontrolservice.aggregate.dto.auth.UserInfoResponse;
import uz.davrbank.educontrolservice.exception.condition.BusinessValidationException;
import uz.davrbank.educontrolservice.exception.condition.ServiceCommunicationException;
import uz.davrbank.educontrolservice.exception.condition.UnauthorizedException;
import uz.davrbank.educontrolservice.exception.dto.ErrorCode;
import uz.davrbank.educontrolservice.feign.security.UserInterfaceFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.security.UserInterface;

import java.util.List;

@Service
@Slf4j
public class UserService implements UserInterface {
    private final UserInterfaceFeign userInterfaceFeign;
    private final JwtTokenService jwtTokenService;

//    private final RestTemplate restTemplate;

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

    public UserService(UserInterfaceFeign userInterfaceFeign,
                       JwtTokenService jwtTokenService
//                       RestTemplate restTemplate
    ) {
        this.userInterfaceFeign = userInterfaceFeign;
        this.jwtTokenService = jwtTokenService;
//        this.restTemplate = restTemplate;
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
    @Override
    public Token login(LoginRequest loginRequest) {
        log.info("Попытка аутентификации пользователя: {}", loginRequest.getUsername());
/*
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", loginRequest.getUsername());
        form.add("password", loginRequest.getPassword());
        form.add("client_secret", clientSecret);
        form.add("client_id", clientId);
        form.add("grant_type", grantType);
        form.add("scope", scope);*/

        Token token = userInterfaceFeign.login(loginRequest);

        // Обогащаем токен информацией о принадлежности к отделу
        boolean isInDepartment = (jwtTokenService.getDepartmentId() != null);
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
    @Override
    public Token refreshToken(RefreshTokenRequest refreshRequest) {
        log.debug("Запрос на обновление токена");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("refresh_token", refreshRequest.getRefreshToken());
        form.add("client_secret", clientSecret);
        form.add("client_id", clientId);
        form.add("grant_type", "refresh_token");

//        Token token = executeTokenRequest(form);

        log.debug("Токен успешно обновлён");
        return null;
    }

    @Override
    public UserInfoResponse getUserInfo() {
        log.info("REQUEST (getUserInfo) FROM [security-service]");
        String token = jwtTokenService.getCurrentAccessToken();
        UserInfoResponse userInfoResponse = userInterfaceFeign.getUserInfo(token);
        userInfoResponse.setDepartment(jwtTokenService.getDepartmentId() != null);
        userInfoResponse.setDepartmentId(jwtTokenService.getDepartmentId());
        return userInfoResponse;
    }

    @Override
    public List<Object> getAllUsers() {
        log.info("REQUEST (getAllUsers) FROM [security-service]");
        return userInterfaceFeign.getAllUsers();
    }


    /**
     * Выполняет запрос к token endpoint Keycloak.
     *
     * @param form параметры запроса
     * @return токен от Keycloak
     */
/*    private Token executeTokenRequest(MultiValueMap<String, String> form) {
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
    }*/
}
