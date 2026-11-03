package uz.davrbank.educontrolservice.controller.security;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.security.group.GroupRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.security.GroupInterface;

@RestController
@RequestMapping("/api/v1/educontrol/security/group")
public class GroupController {
    private final GroupInterface groupInterface;

    public GroupController(GroupInterface groupInterface) {
        this.groupInterface = groupInterface;
    }


    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Object>> getAllGroups() {
        return ResponseEntity.ok(ApiResponse.of("Список групп получен", groupInterface.getAllGroups()));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createGroup(@RequestBody GroupRequestDTO groupRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Группа успешно создана", groupInterface.createGroup(groupRequestDTO)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteGroup(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.of("Группа успешно удалена", groupInterface.deleteGroup(id)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/members/{id}")
    public ResponseEntity<ApiResponse<Object>> getGroupMembers(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.of("Участники группы получены", groupInterface.getGroupMembers(id)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/info/{id}")
    public ResponseEntity<ApiResponse<Object>> getGroupInfo(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о группе получена", groupInterface.getGroupInfo(id)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Object>> updateGroup(@PathVariable String id,
                                                           @RequestBody GroupRequestDTO groupRequestDTO) {
        return ResponseEntity.ok(ApiResponse.of("Группа успешно обновлена", groupInterface.updateGroup(id, groupRequestDTO)));
    }
}
