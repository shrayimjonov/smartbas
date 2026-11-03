package davr.uzcard.Service.Feign;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "svgate-eight", url = "${svgate.additional_url}", configuration = SvgateFeignConfig.class)
public interface SvGateEightFeign {


    @PostMapping
    @Headers("Content-Type: application/json")
    <E> Object connect(@RequestBody E value);

}
