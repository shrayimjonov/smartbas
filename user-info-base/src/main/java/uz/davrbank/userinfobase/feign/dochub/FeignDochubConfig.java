package uz.davrbank.userinfobase.feign.dochub;


import feign.auth.BasicAuthRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;


@Slf4j
public class FeignDochubConfig {

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptorDochub() {
        return new BasicAuthRequestInterceptor("admin", "123456");
    }

}
