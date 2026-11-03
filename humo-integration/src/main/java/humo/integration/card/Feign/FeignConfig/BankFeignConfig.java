package humo.integration.card.Feign.FeignConfig;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class BankFeignConfig {
    @Bean
    public Request.Options bankOptions() {
        return new Request.Options(100, 2000);
    }
}
