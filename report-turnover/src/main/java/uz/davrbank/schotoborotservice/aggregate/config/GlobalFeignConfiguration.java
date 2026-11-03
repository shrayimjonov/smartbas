package uz.davrbank.schotoborotservice.aggregate.config;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@LoadBalancerClients({
        @LoadBalancerClient(name = "schotoborothub-service")
})
public class GlobalFeignConfiguration {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public GlobalFeignConfiguration(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    public Encoder feignEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public Request.Options feignRequestOptions() {
        int connectTimeoutMillis = 120_000;//ms
        int readTimeoutMillis = 1_800_000;//ms
        return new Request.Options(
                connectTimeoutMillis,
                TimeUnit.MILLISECONDS,
                readTimeoutMillis,
                TimeUnit.MILLISECONDS,
                true
        );
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor feignRequestLoggingInterceptor() {
        return template -> {
            String serviceName = null;
            String baseUrl = null;
            try {
                Object target = template.feignTarget();
                if (target != null) {
                    serviceName = (String) target.getClass().getMethod("name").invoke(target);
                    baseUrl = (String) target.getClass().getMethod("url").invoke(target);
                }
            } catch (Throwable ignored) {
                log.error("Failed to get target info: {}", ignored.getMessage());
            }

            String method = template.method();
            String pathAndQuery = template.url();

            String composedUrl = (baseUrl != null && !baseUrl.isBlank())
                    ? (baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl) + pathAndQuery
                    : pathAndQuery;

            log.info("[FEIGN-REQUEST] service={}, method={}, url={}, apiPath={}, headers={}",
                    serviceName != null ? serviceName : "unknown",
                    method,
                    composedUrl,
                    pathAndQuery,
                    template.headers());
        };
    }
}
