package uz.davrbank.educontrolservice.feign.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;

@LoadBalancerClient(name = "course-service", configuration = CourseFeignServiceConfiguration.class)
@Slf4j
public class CourseFeignServiceConfiguration {

    @Bean
    public ErrorDecoder errorDecodeCourse() {
        return new ErrorDecoder.Default();
    }

    @Bean
    Logger.Level feignLoggerLevelCourse() {
        return Logger.Level.FULL;
    }

    public ServiceInstanceListSupplier serviceInstanceListSupplier(DiscoveryClient discoveryClient,
                                                                   FeignClientProperties feignClientProperties) {
        log.info("Course Service List: {} ", discoveryClient.getServices());

        return new CustomServiceInstanceListSupplier(discoveryClient, "course-service");
    }
}
