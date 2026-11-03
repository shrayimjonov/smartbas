package uz.davrbank.educontrolservice.controller.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.security.role.RoleRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.security.RoleInterface;

@RestController
@RequestMapping("/api/v1/educontrol/security/role")
public class RoleController {

    private final RoleInterface roleInterface;

    public RoleController(RoleInterface roleInterface) {
        this.roleInterface = roleInterface;
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Object>> getAllRoles() {
        return ResponseEntity.ok(ApiResponse.of("Список ролей получен",
                roleInterface.getAlLRoles()));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createRole(@RequestBody RoleRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Роль успешно создана",
                        roleInterface.createRole(requestDTO)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PutMapping("/update/{name}")
    public ResponseEntity<ApiResponse<Object>> updateRole(@PathVariable String name, @RequestBody RoleRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.of("Роль успешно обновлена",
                roleInterface.updateRole(name, requestDTO)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<ApiResponse<Object>> deleteRole(@PathVariable String name) {
        return ResponseEntity.ok(ApiResponse.of("Роль успешно удалена",
                roleInterface.deleteRole(name)));
    }
}
