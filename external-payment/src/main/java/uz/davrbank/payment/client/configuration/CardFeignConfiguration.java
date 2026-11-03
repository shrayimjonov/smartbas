package uz.davrbank.payment.client.configuration;


import feign.Logger;
import feign.Request;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class CardFeignConfiguration {

    @Bean
    Logger.Level cardLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder cardErrorDecoder() {
        return new ErrorDecoder.Default();
    }

    @Bean
    public Request.Options cardOptions() {
        return new Request.Options(45000, TimeUnit.MILLISECONDS,
                45000, TimeUnit.MILLISECONDS,
                true);
    }
}
