package nci.billing.feign;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class NciGatewayConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new NciGatewayDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }


}
