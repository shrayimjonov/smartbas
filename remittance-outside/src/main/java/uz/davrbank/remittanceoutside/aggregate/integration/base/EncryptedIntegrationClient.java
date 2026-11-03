/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.integration.base;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;

import java.util.Map;

public interface EncryptedIntegrationClient {

    <REQUEST, ENCRYPTED_OBJECT> ENCRYPTED_OBJECT encrypt(REQUEST request);

    <REQUEST, RESPONSE> RESPONSE postEncrypted(
            String path,
            REQUEST body,
            Map<String, ?> params,
            ParameterizedTypeReference<RESPONSE> responseType,
            HttpHeaders headers);
}
