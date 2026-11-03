package humo.integration.card.Feign.HumoFeign;

import humo.integration.card.Feign.FeignConfig.BankFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "telegramException", url = "${telegram.url}")
public interface TelegramExFeign {

    @PostMapping
    Object sendException(@RequestParam Map<String, String> params);
}
