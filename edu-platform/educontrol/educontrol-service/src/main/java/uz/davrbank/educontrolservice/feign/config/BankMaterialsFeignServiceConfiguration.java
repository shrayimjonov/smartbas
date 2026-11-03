package uz.davrbank.educontrolservice.feign.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;

@LoadBalancerClient(name = "bankmaterials-service", configuration = BankMaterialsFeignServiceConfiguration.class)
@Slf4j
public class BankMaterialsFeignServiceConfiguration {

    @Bean
    public ErrorDecoder errorDecodeBankMaterials() {
        return new ErrorDecoder.Default();
    }

    @Bean
    Logger.Level feignLoggerLevelBankMaterials() {
        return Logger.Level.FULL;
    }

    public ServiceInstanceListSupplier serviceInstanceListSupplier(DiscoveryClient discoveryClient,
                                                                   FeignClientProperties feignClientProperties) {
        log.info("BankMaterials Service List: {} ", discoveryClient.getServices());

        return new CustomServiceInstanceListSupplier(discoveryClient, "bankmaterials-service");
    }
}
