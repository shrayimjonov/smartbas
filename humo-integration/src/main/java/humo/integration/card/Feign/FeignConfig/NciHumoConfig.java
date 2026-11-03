package humo.integration.card.Feign.FeignConfig;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class NciHumoConfig {
    @Value("${bank.humo_api.username}")
    private String username;

    @Value("${bank.humo_api.password}")
    private String password;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }
}
