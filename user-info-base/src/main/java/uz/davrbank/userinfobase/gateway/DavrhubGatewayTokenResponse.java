package uz.davrbank.userinfobase.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.davrbank.userinfobase.client.DavrhubAccessToken;

public record DavrhubGatewayTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn,
        @JsonProperty("scope") String scope
) {

    public DavrhubAccessToken toAccessToken() {
        return new DavrhubAccessToken(
                accessToken,
                tokenType == null ? "Bearer" : tokenType,
                expiresIn
        );
    }
}