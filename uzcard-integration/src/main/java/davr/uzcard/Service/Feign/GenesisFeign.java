package davr.uzcard.Service.Feign;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "genesis", url = "${genesis.url}", configuration = GenesisConfiguration.class)
public interface GenesisFeign {
    @PostMapping
    @Headers("Content-Type: application/json")
    <E> Object connect (@RequestBody E value);
}
