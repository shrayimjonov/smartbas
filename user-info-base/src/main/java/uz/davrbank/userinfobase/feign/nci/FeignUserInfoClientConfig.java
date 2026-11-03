package uz.davrbank.userinfobase.feign.nci;


import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;


public class FeignUserInfoClientConfig {
    @Bean
    public Logger.Level feignLoggerLevelUserInfo() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptorUserInfo() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");

        };
    }

    @Bean
    public Request.Options requestOptionsNCI() {
        return new Request.Options(
                30,
                TimeUnit.SECONDS,
                30,
                TimeUnit.SECONDS,
                true
        );
    }


}
