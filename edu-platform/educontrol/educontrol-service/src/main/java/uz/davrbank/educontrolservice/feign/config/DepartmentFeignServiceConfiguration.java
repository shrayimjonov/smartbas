package uz.davrbank.educontrolservice.feign.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;

@LoadBalancerClient(name = "department-service", configuration = DepartmentFeignServiceConfiguration.class)
@Slf4j
public class DepartmentFeignServiceConfiguration {

    @Bean
    public ErrorDecoder errorDecodeDepartment() {
        return new ErrorDecoder.Default();
    }

    @Bean
    Logger.Level feignLoggerLevelDepartment() {
        return Logger.Level.FULL;
    }

    public ServiceInstanceListSupplier serviceInstanceListSupplier(DiscoveryClient discoveryClient,
                                                                   FeignClientProperties feignClientProperties) {
        log.info("Department Service List: {} ", discoveryClient.getServices());

        return new CustomServiceInstanceListSupplier(discoveryClient, "department-service");
    }

}
