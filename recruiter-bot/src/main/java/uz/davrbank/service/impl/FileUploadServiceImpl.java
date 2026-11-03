package uz.davrbank.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.dto.response.FileUploadResponse;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.FileUpload;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.mapper.FileUploadMapper;
import uz.davrbank.repository.FileUploadRepository;
import uz.davrbank.service.FileUploadService;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    private final FileUploadRepository fileUploadRepository;
    private final FileUploadMapper fileUploadMapper;

    public FileUploadServiceImpl(FileUploadRepository fileUploadRepository, FileUploadMapper fileUploadMapper) {
        this.fileUploadRepository = fileUploadRepository;
        this.fileUploadMapper = fileUploadMapper;
    }

    @Override
    @Transactional
    public ResultData<FileUploadResponse> uploadFile(MultipartFile file) throws IOException {
        FileUpload fileUpload = FileUpload.builder()
                .fileName(file.getOriginalFilename())
                .file(file.getBytes())
                .uploadedAt(LocalDateTime.now())
                .build();
        FileUpload savedFileUpload = fileUploadRepository.save(fileUpload);
        return new ResultData<>(fileUploadMapper.toDto(savedFileUpload));
    }

    @Override
    public ResultData<FileUploadResponse> downloadFile(Long id) {
        FileUpload fileUpload = fileUploadRepository.findById(id).orElseThrow(() -> new NotFoundException("File with ID " + id + " not found."));
        return new ResultData<>(fileUploadMapper.toDto(fileUpload));
    }

    @Override
    public FileUpload findById(Long fileId) {
        return fileUploadRepository.findById(fileId).orElseThrow(() -> new NotFoundException("File with ID " + fileId + " not found."));
    }
}
