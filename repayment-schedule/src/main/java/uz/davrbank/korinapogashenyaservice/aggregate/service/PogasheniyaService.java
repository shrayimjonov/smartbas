package uz.davrbank.korinapogashenyaservice.aggregate.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.response.PogasheniyaGetPageableResponse;
import uz.davrbank.korinapogashenyaservice.aggregate.filter.PogasheniyaFilter;

public interface PogasheniyaService {

    ResponseEntity<PageableIntegrationResponseDTO<PogasheniyaGetPageableResponse>> pogasheniyaGetPageable(
            PogasheniyaFilter filter,
            String username
    );

    ResponseEntity<ByteArrayResource> pogasheniyaDownloadExcel(PogasheniyaFilter filter, String username);
}
