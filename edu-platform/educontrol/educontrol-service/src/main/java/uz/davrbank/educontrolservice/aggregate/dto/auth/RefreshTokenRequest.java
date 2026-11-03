package uz.davrbank.educontrolservice.aggregate.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для запроса обновления токена доступа.
 * Используется для получения нового access_token с помощью refresh_token.
 *
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token обязателен")
    @JsonProperty("refresh_token")
    private String refreshToken;
}

