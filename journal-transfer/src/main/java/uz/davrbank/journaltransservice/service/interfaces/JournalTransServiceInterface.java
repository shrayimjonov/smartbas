package uz.davrbank.journaltransservice.service.interfaces;

import uz.davrbank.journaltransservice.aggregate.dto.request.PageableRequestDTO;
import uz.davrbank.journaltransservice.aggregate.dto.response.PaginatedResponseDTO;

public interface JournalTransServiceInterface {
     PaginatedResponseDTO getJournalTransReportPaginated(PageableRequestDTO pageableRequestDTO);
}
