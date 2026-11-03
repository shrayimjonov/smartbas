package uz.davrbank.partnervalidationservice.client;

public record GatewayAccessToken(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {
}
