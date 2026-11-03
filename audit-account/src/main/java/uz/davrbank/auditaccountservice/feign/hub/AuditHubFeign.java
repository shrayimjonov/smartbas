package uz.davrbank.auditaccountservice.feign.hub;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.auditaccountservice.feign.configure.GlobalFeignConfiguration;


@FeignClient(name = "auditaccounthub-service", contextId = "AuditHubFeign", configuration = GlobalFeignConfiguration.class)
public interface AuditHubFeign {

    @GetMapping("/")
    Object getAllByType(
            @RequestParam String globalType,
            @RequestParam String type,
            @RequestParam Integer page,
            @RequestParam Integer size
    );
}