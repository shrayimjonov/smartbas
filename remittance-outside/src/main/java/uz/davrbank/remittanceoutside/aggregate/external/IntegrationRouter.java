/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.external;

import org.springframework.stereotype.Component;
import uz.davrbank.remittanceoutside.aggregate.integration.base.IntegrationClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class IntegrationRouter {

    private final Map<String, IntegrationClient> clientsMap;


    public IntegrationRouter(List<IntegrationClient> clients) {
        this.clientsMap = clients.stream()
                .collect(Collectors.toUnmodifiableMap(IntegrationClient::getChannelKey, integrationClient -> integrationClient));
    }

    public IntegrationClient resolve(String clientkey) {
        var client = clientsMap.get(clientkey);
        if (client == null) {
            throw new IllegalArgumentException("Client not found " + clientkey);
        }
        return client;
    }
}
