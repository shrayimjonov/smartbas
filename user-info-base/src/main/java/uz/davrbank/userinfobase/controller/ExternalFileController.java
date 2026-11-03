package uz.davrbank.userinfobase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import uz.davrbank.userinfobase.V2.service.AttachmentService;
import uz.davrbank.userinfobase.V2.service.AttachmentSyncService;
import uz.davrbank.userinfobase.dto.ExternalFileDtoResponse;
import uz.davrbank.userinfobase.dto.ExternalFileRequestDto;
import uz.davrbank.userinfobase.dto.response.ResultData;

import java.util.List;

@RestController
@RequestMapping(value = "/api/external-files", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "External Files Controller",
        description = "Endpoints for synchronizing and retrieving files from NCI, DOCHUB and ELMA systems"
)
public class ExternalFileController {

    private final AttachmentSyncService attachmentSyncService;
    private final AttachmentService attachmentService;

    @PostMapping("/sync")
    @Operation(
            summary = "Sync external files",
            description = "Fetch files from NCI (Davrhub), DOCHUB and ELMA systems by loanId and branchId",
            operationId = "syncExternalFiles"
    )
    public ResponseEntity<ResultData<List<ExternalFileDtoResponse>>> syncFiles(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Loan and branch information used to fetch files",
                    required = true
            )
            @Valid @RequestBody ExternalFileRequestDto request

    ) {
        var result = attachmentSyncService.syncAllFiles(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/sync-delay")
    @Operation(
            summary = "Get sync wait delay",
            description = "Returns the current wait delay (in seconds) applied while syncing external files"
    )
    public ResponseEntity<ResultData<Long>> getSyncDelay() {
        return ResponseEntity.ok(new ResultData<>(attachmentSyncService.getSyncDelaySeconds()));
    }

    @PutMapping("/sync-delay")
    @Operation(
            summary = "Update sync wait delay",
            description = "Updates the wait delay (in seconds) applied while syncing external files"
    )
    public ResponseEntity<ResultData<Long>> updateSyncDelay(@RequestParam long seconds) {
        return ResponseEntity.ok(new ResultData<>(attachmentSyncService.updateSyncDelaySeconds(seconds)));
    }

    @GetMapping(value = "/download/{hashId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @Operation(
            summary = "Download Davrhub file",
            description = "Download file stored in MinIO by id"
    )
    public ResponseEntity<StreamingResponseBody> download(@PathVariable String hashId) {
        return attachmentService.download(hashId);
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteExternalFiles() {
        attachmentService.deleteFiles();
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/loan-attachment")
    public ResponseEntity<Void> deleteFileUploads() {
        attachmentSyncService.deleteFileUploads();
        return ResponseEntity.ok().build();
    }
}
