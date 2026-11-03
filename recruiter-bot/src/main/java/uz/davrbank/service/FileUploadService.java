package uz.davrbank.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.dto.response.FileUploadResponse;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.FileUpload;

import java.io.IOException;

public interface FileUploadService {
    ResultData<FileUploadResponse> uploadFile(MultipartFile file) throws IOException;

    ResultData<FileUploadResponse> downloadFile(Long id);

    FileUpload findById(@NotNull(message = "fileId must not be null") Long fileId);
}
