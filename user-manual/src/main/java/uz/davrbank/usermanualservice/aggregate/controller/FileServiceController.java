package uz.davrbank.usermanualservice.aggregate.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.usermanualservice.aggregate.dto.FileDTO;
import uz.davrbank.usermanualservice.aggregate.dto.FileUploadDTO;
import uz.davrbank.usermanualservice.aggregate.model.ResultData;
import uz.davrbank.usermanualservice.aggregate.service.FileService;
import uz.davrbank.usermanualservice.utils.constants.ApiConstants;

import java.util.List;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 27.11.2025
 * Time: 11:11
 * Project: usermanual-service
 */

@RestController
@RequestMapping(ApiConstants.fileServiceRootApi)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileServiceController {

    FileService fileService;

    @PostMapping("/upload/base64")
    public ResponseEntity<ResultData<FileDTO>> uploadFile(@RequestBody FileUploadDTO uploadDTO) {
        return ResponseEntity.ok(fileService.uploadFile(uploadDTO));
    }

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultData<FileDTO>> uploadFileMultipart(
            @RequestPart("file") MultipartFile file,
            @RequestParam("versionId") Long versionId,
            @RequestParam("language") String language
    ) {
        return ResponseEntity.ok(fileService.uploadFile(versionId, language, file));
    }

    @PostMapping(value = "/upload/common",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultData<List<FileDTO>>> uploadCommonFiles(@RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(fileService.uploadCommonFiles(files));
    }

    @GetMapping(value = "/{versionId}/{language}",produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> get(@PathVariable("versionId") Long versionId, @PathVariable("language") String language) {
        return ResponseEntity.ok(fileService.get(versionId,language));
    }
}
