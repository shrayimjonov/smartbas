package uz.davrbank.educontrolservice.feign.security;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.davrbank.educontrolservice.aggregate.dto.security.sync.Sync;
import uz.davrbank.educontrolservice.feign.config.EduexamFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "security-service", contextId = "sync", configuration = EduexamFeignServiceConfiguration.class)
public interface SynchronizeInterfaceFeign {

    @GetMapping("/api/v1/security/sync/components")
    List<Object> getComponents();

    @PostMapping("/api/v1/security/sync")
    Object getSynchronize(@RequestParam String providerId,
                          @RequestParam(value = "action") Sync sync);
}
