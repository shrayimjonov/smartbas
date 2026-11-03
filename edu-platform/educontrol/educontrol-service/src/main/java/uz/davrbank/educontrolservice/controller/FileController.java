package uz.davrbank.educontrolservice.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import uz.davrbank.educontrolservice.aggregate.dto.file.ResourceType;
import uz.davrbank.educontrolservice.aggregate.dto.file.UpdateFileUploadDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.file.FileInterface;

import java.util.List;

//@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DAVR', 'ROLE_HR')")

@RestController
@RequestMapping("/api/v1/educontrol/file")
public class FileController {

    private final FileInterface fileInterface;

    public FileController(FileInterface fileInterface) {
        this.fileInterface = fileInterface;
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Object>> uploadFile(@RequestPart("files") List<MultipartFile> files,
                                                          @RequestPart(value = "descriptions", required = false) String descriptions,
                                                          @RequestParam("resourceType") ResourceType resourceType,
                                                          @RequestParam(value = "resourceId", required = false, defaultValue = "0") Long resourceId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Файлы успешно загружены",
                        fileInterface.uploadFile(files, descriptions, resourceType, resourceId)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_USER')")
    @GetMapping("/info/{id}")
    public ResponseEntity<ApiResponse<Object>> getFileInfo(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о файле получена",
                fileInterface.getFileInfo(id)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_USER')")
    @GetMapping(value = "/download/{id}")
    public ResponseEntity<?> downloadFile(
            @PathVariable Long id,
            @RequestParam(defaultValue = "download") String mode // "view" или "download"
    ) {
        Resource resource = fileInterface.getFileAsResource(id);
        if (resource == null || !resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Определяем content-disposition
        String disposition = mode.equalsIgnoreCase("view") ? "inline" : "attachment";

        // Определяем content-type (можно заменить на probeContentType, если хочешь)
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }


    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_USER')")
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Object>> getAllFiles(@RequestParam(value = "status", required = false) Boolean status) {
        return ResponseEntity.ok(ApiResponse.of("Список файлов получен",
                fileInterface.getAllFilesInfo(status)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_USER', 'ROLE_DEPARTMENT')")
    @GetMapping("/get/all/department/{id}")
    public ResponseEntity<ApiResponse<Object>> getAllFilesByDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Список файлов департамента получен",
                fileInterface.getAllFilesInfoByDepartmentId(id)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteFile(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Файл успешно удалён",
                fileInterface.deleteFile(id)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<Object>> updateFileInfo(@PathVariable Long id, @RequestBody UpdateFileUploadDTO request) {
        return ResponseEntity.ok(ApiResponse.of("Информация о файле обновлена",
                fileInterface.updateFileInfo(id, request)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @GetMapping("/stream/video/{videoId}")
    public ResponseEntity<StreamingResponseBody> streamVideo(@PathVariable Long videoId,
                                                             @RequestParam(value = "start", required = false) Long start,
                                                             @RequestParam(value = "end", required = false) Long end) {
        return fileInterface.streamVideo(videoId, start, end);
    }

}
