package uz.davrbank.payment.client.configuration;


import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import uz.davrbank.payment.model.property.BillingProperty;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BillingConfiguration {
    private final BillingProperty property;
    private final RestTemplate restTemplate;

    @Bean
    Logger.Level billingLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor billingRequestInterceptor() {
        return new BillingFeignRequestInterceptor(property, restTemplate);
    }

    @Bean
    public ErrorDecoder billingErrorDecoder() {
        return new ErrorDecoder.Default();
    }

    @Bean
    public Request.Options billingOptions(){
        return new Request.Options(property.getConnectTimeOut(), TimeUnit.MILLISECONDS,
                property.getReadTimeOut(), TimeUnit.MILLISECONDS,
                true);
    }
}
