package uz.davrbank.userinfobase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.userinfobase.V2.service.AttachmentUploadService;
import uz.davrbank.userinfobase.dto.request.elma.FileUploadBase64Request;
import uz.davrbank.userinfobase.dto.request.elma.FileUploadBase64V2Request;
import uz.davrbank.userinfobase.dto.request.files.FileUploadRequest;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;
import uz.davrbank.userinfobase.enums.ResponseStatus;
import uz.davrbank.userinfobase.service.dochub.DochubService;
import uz.davrbank.userinfobase.service.elma.ElmaService;
import uz.davrbank.userinfobase.service.files.FileUploadService;

import java.util.List;


@RestController
@RequestMapping(value = "/api/getFile", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "File Upload Controller", description = "Endpoints for uploading, downloading and retrieving loan related files")
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final ElmaService elmaService;
    private final DochubService dochubService;
    private final AttachmentUploadService attachmentUploadService;

    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload file",
            description = "Upload file for specific loanExtId",
            operationId = "uploadFile"
    )
    public ResponseEntity<ResultData<FileUploadResponse>> uploadFile(
            @Parameter(description = "File to upload")
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Loan external id")
            @RequestParam("loanExtId") String loanExtId) {

        var result = fileUploadService.uploadFile(file, loanExtId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/upload-base", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Upload base64 file",
            description = "Upload file using base64 encoded string",
            operationId = "uploadFileBase64"
    )
    public ResponseEntity<ResultData<FileUploadResponse>> uploadFileBase64(
            @RequestBody FileUploadRequest request) {

        var result = fileUploadService.uploadFileBase64(request.loanExtId(), request.file());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/upload-elma")
    @Operation(
            summary = "Upload ELMA file",
            description = "Upload file received from ELMA system",
            operationId = "uploadFileElma"
    )
    public ResultData<?> uploadBase64V2(@RequestBody FileUploadBase64Request request) {
        /*return fileUploadService.uploadFileBase64V2(
                request.getLoanExtId(),
                request.getRemainingLoans(),
                String.valueOf(request.getFile())
        );*/
        return new ResultData<>(true, ResponseStatus.SUCCESS, null);
    }

    @PostMapping("/upload-elma-v2")
    @Operation(
            summary = "Upload ELMA files V2",
            description = "Upload multiple files received from ELMA system",
            operationId = "uploadFilesElmaV2"
    )
    public ResultData<?> uploadFilesBase64V2(@Valid @RequestBody FileUploadBase64V2Request request) {
        return attachmentUploadService.uploadElma(
                request
        );
    }

    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @GetMapping("/{id}")
    @Operation(
            summary = "Download file",
            description = "Download file by id",
            operationId = "downloadFile"
    )
    public ResponseEntity<byte[]> downloadFile(
            @Parameter(description = "File id")
            @PathVariable Long id) {

        return fileUploadService.downloadFile(id);
    }

    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @GetMapping("/files")
    @Operation(
            summary = "Get files",
            description = "Returns list of uploaded files by loanExtId",
            operationId = "getAllFiles"
    )
    public ResponseEntity<ResultData<List<FileUploadResponse>>> getAllFiles(
            @Parameter(description = "Loan external id")
            @RequestParam(value = "loanExtId", required = false) String loanExtId) {

        var result = fileUploadService.getAllFiles(loanExtId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @GetMapping("/filesV2")
    @Operation(
            summary = "Get files V2",
            description = "Returns list of files (V2 logic)",
            operationId = "getAllFilesV2"
    )
    public ResponseEntity<ResultData<List<FileUploadResponse>>> getAllFilesV2(
            @Parameter(description = "Loan external id")
            @RequestParam(value = "loanExtId", required = false) String loanExtId) {

        var result = fileUploadService.getAllFilesV2(loanExtId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @GetMapping("/by-loan-id")
    @Operation(
            summary = "Get Dochub files",
            description = "Returns files stored in Dochub by loanId",
            operationId = "getDochubFiles"
    )
    public ResponseEntity<ResultData<List<FileUploadResponse>>> getDochubFiles(
            @Parameter(description = "Loan id")
            @RequestParam String loanId) {

        return ResponseEntity.ok(dochubService.getDochubFilesByLoanId(loanId));
    }

    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @GetMapping("/elma/by-loan-ext-id")
    @Operation(
            summary = "Get ELMA files",
            description = "Returns files uploaded via ELMA system by loanExtId",
            operationId = "getElmaFiles"
    )
    public ResponseEntity<ResultData<List<FileUploadResponse>>> getElmaFilesByLoanExtId(
            @Parameter(description = "Loan external id")
            @RequestParam String loanExtId) {
        return ResponseEntity.ok(elmaService.getAllElmaFilesByLoanExtId(loanExtId));
    }
}
