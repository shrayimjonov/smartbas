/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.utils.constants;

public interface ApiConstants {
    String api = "/api";

    String version = "/v1";
    String integration = "/integration";

    String leskarIntegration = "/leskar";

    String leskarIntegrationRootApi = api + version + integration + leskarIntegration;

    String leskarIntegrationTransferRootApi = leskarIntegrationRootApi + "/transfer";
}
