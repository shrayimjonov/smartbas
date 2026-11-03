package uz.davrbank.educontrolservice.controller.department;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.position.PositionRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.department.PositionInterface;

@RestController
@RequestMapping("/api/v1/educontrol/position")
public class PositionController {
    private final PositionInterface positionInterface;

    public PositionController(PositionInterface positionInterface) {
        this.positionInterface = positionInterface;
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createPosition(@RequestBody PositionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Должность успешно создана",
                        positionInterface.createPosition(request)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Object>> updatePosition(@PathVariable Long id, @RequestBody PositionRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.of("Должность успешно обновлена",
                positionInterface.updatePosition(id, request)));
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Object>> getAllPositions() {
        return ResponseEntity.ok(ApiResponse.of("Список должностей получен",
                positionInterface.getAllPositions()));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deletePosition(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Должность успешно удалена",
                positionInterface.deletePosition(id)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<Object>> getPositionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о должности получена",
                positionInterface.getPositionById(id)));
    }

}
