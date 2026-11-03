package davr.uzcard.Service.Feign;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "svgateTranHistory", url = "${svgate.history_url}", configuration = SvgateFeignConfig.class)
public interface SvGateHistoryFeign {

    @PostMapping
    @Headers("Content-Type: application/json")
    <E> Object svgateConnect(@RequestBody E value);
}
