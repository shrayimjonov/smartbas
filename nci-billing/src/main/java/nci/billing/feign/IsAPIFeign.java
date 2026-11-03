package nci.billing.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "isAPI", url = "${nci.is-api.url}/is_api", configuration = IsAPIConfiguration.class)
public interface IsAPIFeign {
    @PostMapping("/IS_API")
    String baseConnector(@RequestParam Map<String, String> params);


}
