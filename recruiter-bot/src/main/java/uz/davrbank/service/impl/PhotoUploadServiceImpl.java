package uz.davrbank.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.dto.response.PhotoUploadResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.PhotoUpload;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.mapper.PhotoUploadMapper;
import uz.davrbank.repository.PhotoUploadRepository;
import uz.davrbank.service.PhotoUploadService;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class PhotoUploadServiceImpl implements PhotoUploadService {
    private final PhotoUploadRepository photoUploadRepository;
    private final PhotoUploadMapper photoUploadMapper;

    public PhotoUploadServiceImpl(PhotoUploadRepository photoUploadRepository, PhotoUploadMapper photoUploadMapper) {
        this.photoUploadRepository = photoUploadRepository;
        this.photoUploadMapper = photoUploadMapper;
    }

    @Override
    @Transactional
    public ResultData<PhotoUploadResponseDto> uploadPhoto(MultipartFile file) throws IOException {
        PhotoUpload photoUpload = PhotoUpload.builder()
                .photoName(file.getOriginalFilename())
                .photo(file.getBytes())
                .uploadedAt(LocalDateTime.now())
                .build();
        PhotoUpload savedPhotoUpload = photoUploadRepository.save(photoUpload);
        return new ResultData<>(photoUploadMapper.toDto(savedPhotoUpload));
    }

    @Override
    public PhotoUpload findById(Long photoId) {
        return photoUploadRepository.findById(photoId).orElseThrow(() -> new NotFoundException("Photo not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public ResultData<PhotoUploadResponseDto> getPhoto(Long id) {
        PhotoUpload photoUpload = photoUploadRepository.findById(id).orElseThrow(() -> new NotFoundException("Photo not found"));
        PhotoUploadResponseDto dto = photoUploadMapper.toDto(photoUpload);
        return new ResultData<>(dto);
    }
}