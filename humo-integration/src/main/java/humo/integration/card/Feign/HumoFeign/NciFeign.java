package humo.integration.card.Feign.HumoFeign;

import humo.integration.card.Feign.FeignConfig.NciHumoConfig;
import humo.integration.card.Model.Request.CardBlockRequest;
import humo.integration.card.Model.Request.HumoVirtualReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ncihumo", url = "${bank.humo_api.url}", configuration = NciHumoConfig.class)
public interface NciFeign {

    @PostMapping("/humo-cards/api/v2/cards/open-card")
    <E> Object createVirtualCard(@RequestBody HumoVirtualReq request);

    @PostMapping("/humo-cards/api/v2/operations/sms/activate")
    <E> Object onSmsInfo(@RequestBody E value);

    @PostMapping("/humo-cards/api/v2/operations/get-cards/{branch}/{clientId}")
    <E> Object getClientCards(@PathVariable String branch, @PathVariable String clientId);

    @PostMapping("/humo-cards/api/v2/operations/block-card")
    <E> Object blockVirtualCard(@RequestBody CardBlockRequest request);

    @PostMapping("/humo-cards/api/v2/operations/unblock-card")
    <E> Object unBlockVirtualCard(@RequestBody CardBlockRequest request);

}
