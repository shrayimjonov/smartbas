package uz.davrbank.zalogautoservice.service.interfaces;

import uz.davrbank.zalogautoservice.aggregate.dto.*;

public interface AutoServiceInterface {
    AutoResponseDto getAutoZalogReport(AutoRequestDto autoRequestDto);
    AutoPaginatedResponseDto getAutoZalogReportPaginated(AutoRequestDto autoRequestDto);
    AutoPaginatedResponseDto getAutoZalogReportPaginatedSort(AutoPaginatedSortRequestDto autoRequestDto);
}