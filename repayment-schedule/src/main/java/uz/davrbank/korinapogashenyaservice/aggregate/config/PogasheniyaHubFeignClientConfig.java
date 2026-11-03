package uz.davrbank.korinapogashenyaservice.aggregate.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class PogasheniyaHubFeignClientConfig {

    static final int HUB_CONNECT_TIMEOUT_MILLIS = 30_000;
    static final int HUB_READ_TIMEOUT_MILLIS = 1_800_000;

    @Bean
    public Request.Options hubClientRequestOptions() {
        return new Request.Options(
                HUB_CONNECT_TIMEOUT_MILLIS,
                TimeUnit.MILLISECONDS,
                HUB_READ_TIMEOUT_MILLIS,
                TimeUnit.MILLISECONDS,
                true
        );
    }
}
