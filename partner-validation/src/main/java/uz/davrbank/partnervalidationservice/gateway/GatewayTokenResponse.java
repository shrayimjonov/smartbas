package uz.davrbank.partnervalidationservice.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.davrbank.partnervalidationservice.client.GatewayAccessToken;

public record GatewayTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn,
        @JsonProperty("scope") String scope
) {

    public GatewayAccessToken toAccessToken() {
        return new GatewayAccessToken(accessToken, tokenType == null ? "Bearer" : tokenType, expiresIn);
    }
}
