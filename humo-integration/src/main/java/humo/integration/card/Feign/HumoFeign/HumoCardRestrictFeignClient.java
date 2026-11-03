package humo.integration.card.Feign.HumoFeign;

import humo.integration.card.Feign.FeignConfig.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "humo-card-restrict", path = "/cardblock", url = "${humo.middleware}", configuration = {FeignConfig.class})
public interface HumoCardRestrictFeignClient {

    @PostMapping("/v1/get-all-data-from-list")
    Object getBlockedCards();

    @PostMapping("/v1/get-list-item")
    Object getCard(Object request);

    @PostMapping("/v1/add-card")
    Object addCard(Object request);

    @PostMapping("/v1/remove-card")
    Object removeCard(Object request);

}
