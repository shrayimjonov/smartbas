package uz.davrbank.usermanualservice.utils.constants;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

public interface ApiConstants {

    String api = "/api";

    String version = "/v1";

    String manualVersion = "/usermanual-version";
    String manualVersionRootApi = api + version + manualVersion;

    String userManual = "/usermanual-section";
    String fileService = "/files";

    String userManualRootApi = api + version + userManual;
    String fileServiceRootApi = api + version + fileService;

    String id="/{id}";
}
