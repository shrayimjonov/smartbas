/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import uz.davrbank.remittanceoutside.aggregate.enums.IntegrationType;
import uz.davrbank.remittanceoutside.aggregate.external.IntegrationRouter;
import uz.davrbank.remittanceoutside.aggregate.integration.base.EncryptedIntegrationClient;

import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class IntegrationService {

    IntegrationRouter router;

    public <REQUEST, RESPONSE> RESPONSE post(IntegrationType channel,
                                             String path,
                                             REQUEST request,
                                             Map<String, ?> params,
                                             ParameterizedTypeReference<RESPONSE> responseType,
                                             HttpHeaders headers) {
        var client = router.resolve(channel.getName());
        return client.post(path, request, params, responseType, headers);
    }

    public <REQUEST, RESPONSE> RESPONSE postEncrypted(IntegrationType channel,
                                                      String path,
                                                      REQUEST request,
                                                      Map<String, ?> params,
                                                      ParameterizedTypeReference<RESPONSE> responseType,
                                                      HttpHeaders headers) {
        var client = router.resolve(channel.getName());
        if (client instanceof EncryptedIntegrationClient enc) {
            return enc.postEncrypted(path, request, params, responseType, headers);
        }
        throw new IllegalArgumentException("Client does not support encrypted post: " + channel);
    }

    public <RES> RES get(IntegrationType channel,
                         String path,
                         Map<String, ?> params,
                         ParameterizedTypeReference<RES> responseType,
                         HttpHeaders headers) {
        var client = router.resolve(channel.getName());
        return client.get(path, params, responseType, headers);
    }
}
