package uz.davrbank.schotoborotservice.aggregate.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.response.SchotoborotGetPageableResponse;
import uz.davrbank.schotoborotservice.aggregate.filter.SchotoborotFilter;
import uz.davrbank.schotoborotservice.aggregate.service.SchotoborotService;

@RestController
@RequestMapping("/v1/reports")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SchotoborotController {

    SchotoborotService schotoborotService;

    @PostMapping("/get/all/page")
    public ResponseEntity<PageableIntegrationResponseDTO<SchotoborotGetPageableResponse>> getAllPageable(@RequestBody SchotoborotFilter filter, @RequestHeader(value = "username", required = false) String username) {
        return schotoborotService.schotoborotGetPageable(filter, username);
    }

/*    @PostMapping("/download/excel")
    public ResponseEntity<ByteArrayResource> downloadExcel(@RequestBody SchotoborotFilter filter, @RequestHeader(value = "username", required = false) String username) {
        return schotoborotService.schotoborotDownloadExcel(filter, username);
    }*/

/*    @PostMapping("/download/csv")
    public ResponseEntity<ByteArrayResource> downloadCsv(@RequestBody SchotoborotFilter filter, @RequestHeader(value = "username", required = false) String username) {
        return schotoborotService.schotoborotDownloadCsv(filter, username);
    }
    */

    @PostMapping("/download/excel/zip")
    public ResponseEntity<ByteArrayResource> downloadExcelZip(@RequestBody SchotoborotFilter filter, @RequestHeader(value = "username", required = false) String username) {
        return schotoborotService.schotoborotDownloadExcelZip(filter, username);
    }

    @PostMapping("/download/excel")
    public ResponseEntity<ByteArrayResource> downloadCsv(@RequestBody SchotoborotFilter filter, @RequestHeader(value = "username", required = false) String username) {
        return schotoborotService.schotoborotDownloadCsv(filter, username);
    }
}
