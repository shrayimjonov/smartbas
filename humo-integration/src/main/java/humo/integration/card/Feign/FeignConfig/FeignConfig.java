package humo.integration.card.Feign.FeignConfig;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import humo.integration.card.Exception.HumoErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


public class FeignConfig implements RequestInterceptor {

    @Value("${humo.bearer}")
    private String bearer;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new HumoErrorDecoder();
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, bearer));
    }

    @Bean
    public Request.Options FeignOptions() {
        return new Request.Options(10000, 10000);
    }
}
