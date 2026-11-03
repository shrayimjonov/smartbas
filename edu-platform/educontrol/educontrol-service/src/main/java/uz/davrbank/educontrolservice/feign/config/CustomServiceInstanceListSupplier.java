package uz.davrbank.educontrolservice.feign.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
public class CustomServiceInstanceListSupplier implements ServiceInstanceListSupplier {
    private final DiscoveryClient discoveryClient;
    private final String serviceId;

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        return Flux.just(instances);
    }
}
