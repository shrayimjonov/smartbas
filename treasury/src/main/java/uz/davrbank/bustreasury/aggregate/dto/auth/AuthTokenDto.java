package uz.davrbank.bustreasury.aggregate.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthTokenDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 6713493659554496677L;
    @Getter
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refresh_token;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("token_type")
    private String token_type;

    @JsonProperty("expires_in")
    private int expires_in;

}
