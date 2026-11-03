package humo.integration.card.Feign.FeignConfig;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import humo.integration.card.Exception.HumoErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;



@RequiredArgsConstructor
public class FeignCardConfig implements RequestInterceptor {

    @Value("${humo.bearer}")
    private String bearer;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";


    @Bean
    public ErrorDecoder errorCardDecoder() {
        return new HumoErrorDecoder();
    }

    @Bean
    public Logger.Level feignCardLoggerLevel() {
        return Logger.Level.FULL;
    }


    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, bearer));
    }

    @Bean
    public Request.Options CardOptions() {
        return new Request.Options(10000, 10000);
    }

}
