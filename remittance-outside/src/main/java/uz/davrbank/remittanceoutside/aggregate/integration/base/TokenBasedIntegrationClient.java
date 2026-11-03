/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.integration.base;

public interface TokenBasedIntegrationClient extends IntegrationClient {

    String getToken();
}
