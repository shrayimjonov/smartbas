package uz.davrbank.userinfobase.client;

public record DavrhubAccessToken(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {
}