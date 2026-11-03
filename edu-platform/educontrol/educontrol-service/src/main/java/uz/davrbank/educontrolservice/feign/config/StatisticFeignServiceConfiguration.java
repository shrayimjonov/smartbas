package uz.davrbank.educontrolservice.feign.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@LoadBalancerClient(name = "statistic-service", configuration = StatisticFeignServiceConfiguration.class)
public class StatisticFeignServiceConfiguration {

    @Bean
    public ErrorDecoder errorDecodeStatistic() {
        return new ErrorDecoder.Default();
    }

    @Bean
    Logger.Level feignLoggerLevelStatistic() {
        return Logger.Level.FULL;
    }

    public ServiceInstanceListSupplier serviceInstanceListSupplier(DiscoveryClient discoveryClient,
                                                                   FeignClientProperties feignClientProperties) {
        log.info("Statistic Service List: {} ", discoveryClient.getServices());
        return new CustomServiceInstanceListSupplier(discoveryClient, "statistic-service");
    }
}
