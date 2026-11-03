package uz.davrbank.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ArchiveResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.Archive;
import uz.davrbank.enums.LanguageType;
import uz.davrbank.enums.ResponseStatus;
import uz.davrbank.enums.SearchType;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.mapper.ArchiveMapper;
import uz.davrbank.repository.ArchiveRepository;
import uz.davrbank.repository.specification.ArchiveSpecification;
import uz.davrbank.service.ArchiveService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveServiceImpl implements ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final ArchiveMapper archiveMapper;

    @Override
    public void saveArchive(Archive archive) {
        archiveRepository.save(archive);
    }

    @Override
    public ResultData<PaginationResponse<ArchiveResponseDto>> getAllArchievedMessages(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Specification<Archive> specification = ArchiveSpecification.orderByIdDesc();
        Page<Archive> archivedMessages = archiveRepository.findAll(specification, pageable);
        PaginationResponse<ArchiveResponseDto> paginationResponse = PaginationResponse.fromPage(archivedMessages, archiveMapper::toDto);
        return new ResultData<>(paginationResponse);
    }


    @Override
    public ResultData<List<ArchiveResponseDto>> searchArchivedMessages(LanguageType language, SearchType type, String text) {
        if (text.isBlank() || text.trim().isEmpty()) {
            return new ResultData<>(false, ResponseStatus.TEXT_DATA, "Text should not be empty.");
        }
        Specification<Archive> specification = ArchiveSpecification.searchByLanguageAndType(language.getCode(), type.getType(), text);
        List<Archive> archives = archiveRepository.findAll(specification);
        List<ArchiveResponseDto> responseList = archives.stream().map(archiveMapper::toDto).collect(Collectors.toList());
        if (responseList.isEmpty()) {
            return new ResultData<>(false, ResponseStatus.INCORRECT_DATA, "No matching archived messages found.");
        }
        return new ResultData<>(responseList);
    }

    @Override
    @Transactional
    public ResultData<ResponseDto> deleteArchiveById(Long id) {
        if (!archiveRepository.existsById(id)) {
            throw new NotFoundException("Archive with id " + id + " not found.");
        }
        archiveRepository.deleteById(id);
        return new ResultData<>(ResponseDto.builder().code(200).message("Archive entry deleted successfully.").build());
    }

}

