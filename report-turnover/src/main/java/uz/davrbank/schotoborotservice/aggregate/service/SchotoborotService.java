package uz.davrbank.schotoborotservice.aggregate.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.response.SchotoborotGetPageableResponse;
import uz.davrbank.schotoborotservice.aggregate.filter.SchotoborotFilter;

public interface SchotoborotService {

    ResponseEntity<ByteArrayResource> schotoborotDownloadExcel(SchotoborotFilter filter, String username);

    ResponseEntity<ByteArrayResource> schotoborotDownloadCsv(SchotoborotFilter filter, String username);

    ResponseEntity<ByteArrayResource> schotoborotDownloadExcelZip(SchotoborotFilter filter, String username);

    ResponseEntity<PageableIntegrationResponseDTO<SchotoborotGetPageableResponse>> schotoborotGetPageable(SchotoborotFilter filter, String username);
}
