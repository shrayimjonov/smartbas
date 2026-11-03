package uz.davrbank.service;

import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.dto.response.PhotoUploadResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.PhotoUpload;

import java.io.IOException;

public interface PhotoUploadService {
    ResultData<PhotoUploadResponseDto> uploadPhoto(MultipartFile file) throws IOException;

    PhotoUpload findById(Long photoId);

    ResultData<PhotoUploadResponseDto> getPhoto(Long id);
}
