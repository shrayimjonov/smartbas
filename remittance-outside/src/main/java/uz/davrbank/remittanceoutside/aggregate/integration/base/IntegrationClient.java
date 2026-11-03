/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.integration.base;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;

import java.time.Duration;
import java.util.Map;

public interface IntegrationClient {

    String getChannelKey();

    Duration getTimeout();

    <REQUEST, RESPONSE> RESPONSE post(
            String path,
            REQUEST body,
            Map<String, ?> params,
            ParameterizedTypeReference<RESPONSE> responseType,
            HttpHeaders headers);

    <RESPONSE> RESPONSE get(
            String path,
            Map<String, ?> params,
            ParameterizedTypeReference<RESPONSE> responseType,
            HttpHeaders headers);

    default HttpHeaders getHeaders(HttpHeaders headers) {
        return headers != null ? headers : new HttpHeaders();
    }
}
