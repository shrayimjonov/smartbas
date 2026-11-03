package humo.integration.card.Exception;

public class ApiErrorMessages {
    public static final String BAD_REQUEST = "Invalid request. Please check request once more. ";
    public static final String UNAUTHORIZED = "You are not authorized";
    public static final String NOT_FOUND = "Could not find requesting data. ";
    public static final String INTERNAL_SERVER_ERROR = "Error occurs while exchange data with database. Please try again later, after checking given error details. ";
    public static final String UNSUPPORTED_MEDIA_TYPE = "Error occurs cause coming unsupported media type. Please send shown body only. ";
    public static final String METHOD_NOT_ALLOWED = "Used not valid http method. Please check it. ";
    public static final String EXCEPTION_FROM_THIRD_PARTY = "Error occurs while calling third party api. ";
    public static final String REQUEST_TIME_EXCEPTION = "Request timeout exception occurs. Please check third party server.";
    public static final String EXCEPTION_SIGN_GENERATOR = "Error occurs while creating sign.";
}
