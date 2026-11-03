package uz.davrbank.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.config.annotations.fileValidation.FileType;
import uz.davrbank.config.annotations.fileValidation.FileTypeValid;
import uz.davrbank.dto.response.PhotoUploadResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.service.PhotoUploadService;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1")
public class PhotoUploadController {
    private final PhotoUploadService photoUploadService;

    public PhotoUploadController(PhotoUploadService photoUploadService) {
        this.photoUploadService = photoUploadService;
    }

    @PostMapping(
            value = "/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResultData<PhotoUploadResponseDto>> uploadFile(
            @FileTypeValid(
                    allowedTypes = {FileType.PNG, FileType.JPEG, FileType.JPG},
                    message = "File must be image (PNG/JPEG/JPG) format"
            )
            @RequestParam("file") MultipartFile file) throws IOException {
        var result = photoUploadService.uploadPhoto(file);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<ResultData<PhotoUploadResponseDto>> getPhoto(@PathVariable Long id) {
        return ResponseEntity.ok(photoUploadService.getPhoto(id));
    }
}
