package uz.davrbank.educontrolservice.controller.cloudresource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.library.LibraryRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.cloudresource.LibraryInterface;

import java.util.List;

@RestController
@RequestMapping("/api/v1/educontrol/library")
public class LibraryController {

    private final LibraryInterface libraryInterface;

    public LibraryController(LibraryInterface libraryInterface) {
        this.libraryInterface = libraryInterface;
    }


    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createLibrary(@RequestBody LibraryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Библиотека успешно создана",
                        libraryInterface.createLibrary(dto)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Object>> updateLibrary(@PathVariable Long id, @RequestBody LibraryRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.of("Библиотека успешно обновлена",
                libraryInterface.updateLibrary(id, dto)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Object>> getAllLibraries(@RequestParam(required = false) String categoryId,
                                                               @RequestParam(required = false) List<Long> departmentIds,
                                                               @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(ApiResponse.of("Список библиотек получен",
                libraryInterface.getAllLibraries(categoryId, departmentIds, page, size)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @GetMapping("/get/one/{id}")
    public ResponseEntity<ApiResponse<Object>> getOneLibrary(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о библиотеке получена",
                libraryInterface.getOneLibrary(id)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @GetMapping("get/data/{id}")
    public ResponseEntity<ApiResponse<Object>> getLibraryData(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Данные библиотеки получены",
                libraryInterface.getLibraryData(id)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteLibrary(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Библиотека успешно удалена",
                libraryInterface.deleteLibrary(id)));
    }

}
