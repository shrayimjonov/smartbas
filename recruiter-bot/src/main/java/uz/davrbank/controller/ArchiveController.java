package uz.davrbank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ArchiveResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.enums.LanguageType;
import uz.davrbank.enums.SearchType;
import uz.davrbank.service.ArchiveService;

import java.util.List;

@RestController
@RequestMapping("/api/archieve")

public class ArchiveController {

    private final ArchiveService archiveService;

    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @GetMapping()
    public ResponseEntity<ResultData<PaginationResponse<ArchiveResponseDto>>> getAllArchivedMessages(PaginationRequest request) {
        var result = archiveService.getAllArchievedMessages(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<ResultData<List<ArchiveResponseDto>>> searchArchivedMessages(
            @RequestParam(value = "language") LanguageType language,
            @RequestParam(value = "type") SearchType type,
            @RequestParam(value = "text") String text) {
        var result = archiveService.searchArchivedMessages(language, type, text);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultData<ResponseDto>> deleteArchive(@PathVariable Long id) {
        var result = archiveService.deleteArchiveById(id);
        return ResponseEntity.ok(result);
    }

}
