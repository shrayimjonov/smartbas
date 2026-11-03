package uz.davrbank.educontrolservice.feign.security;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.security.role.RoleRequestDTO;
import uz.davrbank.educontrolservice.feign.config.EduexamFeignServiceConfiguration;

@FeignClient(name = "security-service", contextId = "role", configuration = EduexamFeignServiceConfiguration.class)
public interface RoleInterfaceFeign {

    @GetMapping("/api/v1/security/role/get/all")
    Object getAllRoles();

    @PostMapping("/api/v1/security/role/create")
    Object createRole(@RequestBody RoleRequestDTO requestDTO);

    @PutMapping("/api/v1/security/role/update/{name}")
    Object updateRole(@PathVariable String name,
                      @RequestBody RoleRequestDTO requestDTO);

    @DeleteMapping("/api/v1/security/role/delete/{name}")
    Object deleteRole(@PathVariable String name);

}
