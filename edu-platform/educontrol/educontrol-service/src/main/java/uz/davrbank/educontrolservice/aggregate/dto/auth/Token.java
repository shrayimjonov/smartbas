package uz.davrbank.educontrolservice.aggregate.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("expires_in")
    Long expiresIn;

    @JsonProperty("refresh_expires_in")
    Long refreshExpiresIn;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("id_token")
    String idToken;

    @JsonProperty("not-before-policy")
    Long notBeforePolicy;

    @JsonProperty("session_state")
    String sessionState;

    @JsonProperty("scope")
    String scope;

    @JsonProperty("is_department")
    boolean isDepartment;
}
