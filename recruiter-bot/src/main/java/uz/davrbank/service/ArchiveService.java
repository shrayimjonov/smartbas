package uz.davrbank.service;

import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ArchiveResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.Archive;
import uz.davrbank.enums.LanguageType;
import uz.davrbank.enums.SearchType;

import java.util.List;

public interface ArchiveService {
    void saveArchive(Archive archive);

    ResultData<PaginationResponse<ArchiveResponseDto>> getAllArchievedMessages(PaginationRequest paginationRequest);

    ResultData<List<ArchiveResponseDto>> searchArchivedMessages(LanguageType language, SearchType type, String text);

    ResultData<ResponseDto> deleteArchiveById(Long id);


}
