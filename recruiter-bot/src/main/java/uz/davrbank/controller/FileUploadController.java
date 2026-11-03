package uz.davrbank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.config.annotations.fileValidation.FileType;
import uz.davrbank.config.annotations.fileValidation.FileTypeValid;
import uz.davrbank.dto.response.FileUploadResponse;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.service.FileUploadService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(
            value = "/createResume",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResultData<FileUploadResponse>> uploadFile(
            @FileTypeValid(
                    allowedTypes = {FileType.PDF},
                    message = "File must be PDF "
            ) @RequestParam("file") MultipartFile file) throws IOException {
        var resultData = fileUploadService.uploadFile(file);
        return ResponseEntity.ok(resultData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        ResultData<FileUploadResponse> result = fileUploadService.downloadFile(id);
        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        FileUploadResponse response = result.getData();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + response.getFileName() + "\"")
                .body(response.getFile());
    }

}
