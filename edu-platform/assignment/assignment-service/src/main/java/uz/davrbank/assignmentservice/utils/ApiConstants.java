
package uz.davrbank.assignmentservice.utils;

public final class ApiConstants {

    private ApiConstants() {
    }

    final static String api = "/api";

    final static String version = "/v1";

    final static String serviceName = "/assignment-service";

    // domains
    final static String categoryApi = "/category";
    final static String typeApi = "/type";
    final static String taskApi = "/task";

    //api roots
    public final static String categoryRootApi = serviceName + api + version + categoryApi;
    public final static String typeRootApi = serviceName + api + version + typeApi;
    public final static String taskRootApi = serviceName + api + version + taskApi;

    // elements
    public final static String id = "/{id}";
    public final static String list = "/list";
    public final static String status = "/{id}/status";
    public final static String statuses = "/statuses";

}