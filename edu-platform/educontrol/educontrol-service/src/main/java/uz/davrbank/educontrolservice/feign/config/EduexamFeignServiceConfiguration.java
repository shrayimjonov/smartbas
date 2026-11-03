package uz.davrbank.educontrolservice.feign.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;

@LoadBalancerClient(name = "eduexam-service", configuration = EduexamFeignServiceConfiguration.class)
@Slf4j
public class EduexamFeignServiceConfiguration {
    private final JwtTokenService jwtTokenService;

    public EduexamFeignServiceConfiguration(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Bean
    public ErrorDecoder errorDecodeEduexam() {
        return new ErrorDecoder.Default();
    }

    @Bean
    Logger.Level feignLoggerLevelEduexam() {
        return Logger.Level.FULL;
    }

    public ServiceInstanceListSupplier serviceInstanceListSupplier(DiscoveryClient discoveryClient,
                                                                   FeignClientProperties feignClientProperties) {
        log.info("Eduexam Service List: {} ", discoveryClient.getServices());
        return new CustomServiceInstanceListSupplier(discoveryClient, "eduexam-service");
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("X-Department-Id", String.valueOf(1));
            /*Long deptId = jwtTokenService.getDepartmentId();
            if (deptId != null) {
            }*/
        };
    }
}

