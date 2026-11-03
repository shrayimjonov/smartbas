package uz.davrbank.partnervalidationservice.constants;

public final class AuthConstants {

    private AuthConstants() {
    }

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String SERVICE_NAME_HEADER = "X-Service-Name";
}
