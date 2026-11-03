package uz.davrbank.educontrolservice.feign.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;

@LoadBalancerClient(name = "assignment-service", configuration = AssignmentFeignServiceConfiguration.class)
@Slf4j
public class AssignmentFeignServiceConfiguration {

    @Bean
    public ErrorDecoder errorDecodeAssigment() {
        return new ErrorDecoder.Default();
    }

    @Bean
    Logger.Level feignLoggerLevelAssigment() {
        return Logger.Level.FULL;
    }

    public ServiceInstanceListSupplier serviceInstanceListSupplier(DiscoveryClient discoveryClient,
                                                                   FeignClientProperties feignClientProperties) {
        log.info("Assigment Service List: {} ", discoveryClient.getServices());

        return new CustomServiceInstanceListSupplier(discoveryClient, "assignment-service");
    }
}
