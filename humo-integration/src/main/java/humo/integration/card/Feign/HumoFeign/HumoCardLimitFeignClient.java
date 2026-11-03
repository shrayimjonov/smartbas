package humo.integration.card.Feign.HumoFeign;


import humo.integration.card.Feign.FeignConfig.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "humo-card-limit", path = "/cardblock", url = "${humo.middleware}", configuration = {FeignConfig.class})
public interface HumoCardLimitFeignClient {

    @PostMapping("/v2/get-limit-balance")
    Object getLimitBalance(Object request);


    @PostMapping("/v2/get-ref-data")
    Object getRefData();

    @PostMapping("/v2/set-any-limit")
    Object setAnyLimit(Object request);

    @PostMapping("/v2/edit-any-limit")
    Object editAnyLimit(Object request);

    @PostMapping("/v2/get-subject-value")
    Object getSubjectValue(Object request);

    @PostMapping("/v2/delete-limit")
    Object deleteLimit(Object request);

    @PostMapping("/v2/delete-all-limit")
    Object deleteAllLimit(Object request);

}
