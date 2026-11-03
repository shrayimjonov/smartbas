package uz.davrbank.getoutsideloanservice.aggregate.config;

import feign.Logger;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Configuration(proxyBeanMethods = false)
@LoadBalancerClients({
        @LoadBalancerClient(name = "getoutsideloanhub-service")
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
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new FeignErrorDecoder(objectMapper);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor feignRequestLoggingInterceptor() {
        return template -> {
            String serviceName = "unknown";
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

            String pathAndQuery = template.url();
            String composedUrl = (baseUrl != null && !baseUrl.isBlank())
                    ? (baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl) + pathAndQuery
                    : pathAndQuery;

            log.info(
                    "[FEIGN-REQUEST] service={}, method={}, url={}, apiPath={}, headers={}",
                    serviceName,
                    template.method(),
                    composedUrl,
                    pathAndQuery,
                    template.headers()
            );
        };
    }
}
