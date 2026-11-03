package nci.billing.feign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import nci.billing.model.property.IsApiProps;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class IsAPIConfiguration {
    private final IsApiProps properties;

    @Bean
    public ErrorDecoder errorDecoder() {
        return new NciGatewayDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("login", properties.getUsername());
            requestTemplate.header("password", properties.getPassword());
            requestTemplate.header("branch", properties.getBranch());
        };
    }


}
