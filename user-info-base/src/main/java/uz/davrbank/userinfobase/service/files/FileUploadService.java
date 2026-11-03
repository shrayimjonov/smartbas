package uz.davrbank.userinfobase.service.files;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;

import java.util.List;

public interface FileUploadService {

    ResultData<FileUploadResponse> uploadFile(MultipartFile file, String loanExtId);

    ResponseEntity<byte[]> downloadFile(Long id);

    ResultData<List<FileUploadResponse>> getAllFiles(String loanExtId);

    ResultData<List<FileUploadResponse>> getAllFilesV2(String loanExtId);

    ResultData<FileUploadResponse> uploadFileBase64(String loanExtId, String file);

    ResultData<List<FileUploadResponse>> uploadFileBase64V2(String loanExtId, Integer remainingLoans, String file);

    ResultData<List<FileUploadResponse>> uploadFilesBase64V2(String loanExtId, Integer remainingLoans, List<String> files, String branchId);


}
