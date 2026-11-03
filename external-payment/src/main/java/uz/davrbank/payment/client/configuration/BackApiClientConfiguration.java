package uz.davrbank.payment.client.configuration;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import uz.davrbank.payment.model.property.BackApiProperty;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class BackApiClientConfiguration {
    private final BackApiProperty property;

    @Bean
    public ErrorDecoder backErrorDecoder() {
        return new ErrorDecoder.Default();
    }

    @Bean
    public Logger.Level backFeignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor backRequestInterceptor() {
        log.info("Back API using username: {}", property.getUsername());
        return requestTemplate -> {
            requestTemplate.header("username", property.getUsername());
            requestTemplate.header("password", property.getPassword());
        };
    }

    @Bean
    public Request.Options backRequestOptions() {
        var requestOptions = new Request.Options(
                property.getConnectTimeOut(),
                TimeUnit.MILLISECONDS,
                property.getReadTimeOut(),
                TimeUnit.MILLISECONDS,
                true
        );
        log.info("Using request options: {} for BACK API info client", requestOptions);
        return requestOptions;
    }

}
