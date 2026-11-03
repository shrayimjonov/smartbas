package uz.davrbank.userinfobase.feign.elma;

import feign.Logger;
import feign.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;



@Slf4j
public class FeignElmaConfig {

    @Bean
    public Logger.Level feignLoggerLevelElma() {
        return Logger.Level.FULL;
    }


    @Bean
    public Request.Options requestOptionsELma() {
        return new Request.Options(
                30,
                TimeUnit.SECONDS,
                30,
                TimeUnit.SECONDS,
                true
        );
    }
}
