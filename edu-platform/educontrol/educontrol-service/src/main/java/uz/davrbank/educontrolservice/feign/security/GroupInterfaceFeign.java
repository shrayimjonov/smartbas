package uz.davrbank.educontrolservice.feign.security;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.security.group.GroupRequestDTO;
import uz.davrbank.educontrolservice.feign.config.EduexamFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "security-service", contextId = "group", configuration = EduexamFeignServiceConfiguration.class)
public interface GroupInterfaceFeign {

    @GetMapping("/api/v1/security/group/get/all")
    List<Object> getAllGroups();

    @PostMapping("/api/v1/security/group/create")
    Object createGroup(@RequestBody GroupRequestDTO groupRequestDTO);

    @PutMapping("/api/v1/security/group/update/{id}")
    Object updateGroup(@PathVariable String id,
                       @RequestBody GroupRequestDTO groupRequestDTO);

    @DeleteMapping("/api/v1/security/group/delete/{id}")
    Object deleteGroup(@PathVariable String id);

    @GetMapping("/api/v1/security/group/get/members/{id}")
    List<Object> getGroupMembers(@PathVariable String id);

    @GetMapping("/api/v1/security/group/get/info/{id}")
    Object getGroupInfo(@PathVariable String id);
}
