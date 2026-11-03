package uz.davrbank.partnervalidationservice.constants;

public final class ApiConstants {

    private ApiConstants() {
    }

    public static final String API = "/api";
    public static final String VERSION = "/v1";
    public static final String SERVICE_NAME = "/partner-validation-service";
    public static final String ROOT_API = API + VERSION + SERVICE_NAME;

    public static final String MERCHANTS = "/merchants";
    public static final String ACCOUNTS = "/accounts";
    public static final String LOGS = "/logs";
    public static final String VALIDATE = "/validate";

    public static final String ID = "/{id}";
    public static final String LOOKUP = "/lookup";

    public static final String VALIDATION_ROOT_API = ROOT_API;
    public static final String MERCHANTS_ROOT_API = ROOT_API + MERCHANTS;
    public static final String MERCHANT_ACCOUNTS_ROOT_API = MERCHANTS_ROOT_API + ACCOUNTS;
    public static final String LOGS_ROOT_API = ROOT_API + LOGS;
}
