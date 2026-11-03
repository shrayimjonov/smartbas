package uz.davrbank.educontrolservice.feign.security;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.security.role.RolesRequestDTO;
import uz.davrbank.educontrolservice.feign.config.EduexamFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "security-service", contextId = "merge", configuration = EduexamFeignServiceConfiguration.class)
public interface MergeInterfaceFeign {

    @PostMapping("/api/v1/security/merge/role/group/{groupId}")
    Object mergeRolesToGroup(@PathVariable String groupId,
                             @RequestBody List<RolesRequestDTO> rolesRequestDTOList);

    @PostMapping("/api/v1/security/merge/user/group")
    Object mergeUserToGroup(@RequestParam String userId,
                            @RequestParam String groupId);

    @DeleteMapping("/api/v1/security/merge/delete/role/group/{groupId}")
    Object deleteRolesFromGroup(@PathVariable String groupId,
                                @RequestBody List<RolesRequestDTO> rolesRequestDTOList);

    @DeleteMapping("/api/v1/security/merge/delete/user/group")
    Object deleteUserFromGroup(@RequestParam String userId,
                               @RequestParam String groupId);
}
