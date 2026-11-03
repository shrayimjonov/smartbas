package uz.davrbank.korinapogashenyaservice.aggregate.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.response.PogasheniyaGetPageableResponse;
import uz.davrbank.korinapogashenyaservice.aggregate.filter.PogasheniyaFilter;
import uz.davrbank.korinapogashenyaservice.aggregate.service.PogasheniyaService;

@RestController
@RequestMapping("/v1/reports")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PogasheniyaController {

    PogasheniyaService pogasheniyaService;

    @PostMapping("/get/all/page")
    public ResponseEntity<PageableIntegrationResponseDTO<PogasheniyaGetPageableResponse>> getAllPageable(
            @RequestBody PogasheniyaFilter filter,
            @RequestHeader(value = "username", required = false) String username
    ) {
        return pogasheniyaService.pogasheniyaGetPageable(filter, username);
    }

    @PostMapping("/download/excel")
    public ResponseEntity<ByteArrayResource> downloadExcel(
            @RequestBody PogasheniyaFilter filter,
            @RequestHeader(value = "username", required = false) String username
    ) {
        return pogasheniyaService.pogasheniyaDownloadExcel(filter, username);
    }
}
