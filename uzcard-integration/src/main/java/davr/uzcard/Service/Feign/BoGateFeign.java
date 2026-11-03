package davr.uzcard.Service.Feign;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "boGate", url = "${bogate.url}", configuration = BoGateConfiguration.class)
public interface BoGateFeign {

    @PostMapping
    @Headers("Content-Type: application/json")
    <E> Object boGateConnect(@RequestBody E value);
}
