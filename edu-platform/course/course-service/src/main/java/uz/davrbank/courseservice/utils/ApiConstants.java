package uz.davrbank.courseservice.utils;

public final class ApiConstants {

    private ApiConstants() {
    }

    private static final String api = "/api";
    private static final String version = "/v1";
    private static final String serviceName = "/course-service";

    private static final String courseApi = "/course";
    private static final String courseEmployeeApi = "/course/employee";
    private static final String stepApi = "/step";
    private static final String stepEmployeeApi = "/step/employee";
    private static final String stepTextBlockApi = "/step/text-blocks";

    public static final String courseRootApi = serviceName + api + version + courseApi;
    public static final String courseEmployeeRootApi = serviceName + api + version + courseEmployeeApi;
    public static final String stepRootApi = serviceName + api + version + stepApi;
    public static final String stepEmployeeRootApi = serviceName + api + version + stepEmployeeApi;
    public static final String stepTextBlockRootApi = serviceName + api + version + stepTextBlockApi;

    public static final String id = "/{id}";
    public static final String list = "/list";
    public static final String access = "/{id}/access";
    public static final String start = "/start";
    public static final String complete = "/complete";
    public static final String files = "/files";
}
