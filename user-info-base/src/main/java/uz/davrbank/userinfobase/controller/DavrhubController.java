package uz.davrbank.userinfobase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import uz.davrbank.userinfobase.dto.DavrhubDtoResponse;
import uz.davrbank.userinfobase.dto.request.DavrhubFileListRequest;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.exceptions.GeneralException;
import uz.davrbank.userinfobase.service.DavrhubService;
import uz.davrbank.userinfobase.service.HashIdService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/davrhub", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Davrhub Controller", description = "Endpoints for integration with Davrhub document service")
public class DavrhubController {
    private final DavrhubService davrhubService;
    private final HashIdService hashIdService;

    @PostMapping(value = "/files", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @Operation(
            summary = "Get document list from Davrhub",
            description = "Requests document list from Davrhub service using transaction id and branch",
            operationId = "getDavrhubFiles"
    )
    public ResponseEntity<ResultData<List<DavrhubDtoResponse>>> getFiles(
            @Valid @RequestBody DavrhubFileListRequest request
    ) {
        var result = davrhubService.getFiles(request);

        return ResponseEntity.ok(new ResultData<>(result));
    }

    @GetMapping("/download/{hashId}")
//    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @Operation(
            summary = "Download Davrhub file",
            description = "Download file stored in MinIO by id"
    )
    public ResponseEntity<StreamingResponseBody> download(@PathVariable String hashId) {
        Long id;
        try {
            id = hashIdService.decode(hashId);
        } catch (Exception e) {
            throw new GeneralException("Invalid file id!!!");
        }
        return davrhubService.downloadFile(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @Operation(
            summary = "Delete Davrhub file",
            description = "Deletes file from MinIO and database by id"
    )
    public ResponseEntity<ResultData<String>> deleteFile(@PathVariable Long id) {
        davrhubService.deleteFile(id);
        return ResponseEntity.ok(new ResultData<>("File deleted successfully"));
    }

}
