package uz.davrbank.educontrolservice.controller.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.security.role.RolesRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.security.MergeInterface;

import java.util.List;

@RestController
@RequestMapping("/api/v1/educontrol/security/merge")
public class MergeController {
    private final MergeInterface mergeInterface;

    public MergeController(MergeInterface mergeInterface) {
        this.mergeInterface = mergeInterface;
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping("/role/{groupId}")
    public ResponseEntity<ApiResponse<Object>> mergeRoles(@PathVariable String groupId,
                                                          @RequestBody List<RolesRequestDTO> rolesRequestDTOS) {
        return ResponseEntity.ok(ApiResponse.of("Роли успешно привязаны к группе",
                mergeInterface.mergeRoleToGroup(groupId, rolesRequestDTOS)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping("/user")
    public ResponseEntity<ApiResponse<Object>> mergeUser(@RequestParam("userId") String userId,
                                                         @RequestParam("groupId") String groupId) {
        return ResponseEntity.ok(ApiResponse.of("Пользователь успешно добавлен в группу",
                mergeInterface.mergeUserToGroup(userId, groupId)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/role/{groupId}")
    public ResponseEntity<ApiResponse<Object>> deleteRole(@PathVariable String groupId,
                                                          @RequestBody List<RolesRequestDTO> rolesRequestDTOS) {
        return ResponseEntity.ok(ApiResponse.of("Роли успешно удалены из группы",
                mergeInterface.deleteRolesGroupGroup(groupId, rolesRequestDTOS)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@RequestParam("userId") String userId,
                                                          @RequestParam("groupId") String groupId) {
        return ResponseEntity.ok(ApiResponse.of("Пользователь успешно удалён из группы",
                mergeInterface.deleteUserFromGroup(userId, groupId)));
    }
}
