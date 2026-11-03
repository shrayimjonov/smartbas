package uz.davrbank.zalogipatekaservice.feign.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public record CustomServiceInstanceListSupplier(DiscoveryClient discoveryClient,
                                                String serviceId) implements ServiceInstanceListSupplier {

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        log.info("Resolved instances for {}: {}", serviceId, instances);
        return Flux.just(instances);
    }
}
