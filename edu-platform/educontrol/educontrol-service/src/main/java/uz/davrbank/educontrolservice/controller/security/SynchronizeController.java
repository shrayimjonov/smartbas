package uz.davrbank.educontrolservice.controller.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.security.sync.Sync;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.security.SynchronizeInterface;

@RestController
@RequestMapping("/api/v1/educontrol/security/sync")
public class SynchronizeController {
    private final SynchronizeInterface synchronizeInterface;

    public SynchronizeController(SynchronizeInterface synchronizeInterface) {
        this.synchronizeInterface = synchronizeInterface;
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/components")
    public ResponseEntity<ApiResponse<Object>> getComponents() {
        return ResponseEntity.ok(ApiResponse.of("Список компонентов получен",
                synchronizeInterface.getProviders()));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping("")
    public ResponseEntity<ApiResponse<Object>> synchronize(@RequestParam(value = "action", defaultValue = "FULL") Sync sync) {
        return ResponseEntity.ok(ApiResponse.of("Синхронизация выполнена успешно",
                synchronizeInterface.userSync(null, sync)));
    }
}
