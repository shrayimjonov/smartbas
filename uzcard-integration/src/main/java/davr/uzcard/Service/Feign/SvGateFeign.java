package davr.uzcard.Service.Feign;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "svgate", url = "${svgate.url}", configuration = SvgateFeignConfig.class)
public interface SvGateFeign {


    @PostMapping
    @Headers("Content-Type: application/json")
    <E> Object svgateConnect(@RequestBody E value);

}
