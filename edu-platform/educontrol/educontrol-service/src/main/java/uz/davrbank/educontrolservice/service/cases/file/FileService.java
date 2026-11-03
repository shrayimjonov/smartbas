package uz.davrbank.educontrolservice.service.cases.file;

import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import uz.davrbank.educontrolservice.aggregate.dto.file.ResourceType;
import uz.davrbank.educontrolservice.aggregate.dto.file.UpdateFileUploadDTO;
import uz.davrbank.educontrolservice.feign.BankMaterialsClientFeign;
import uz.davrbank.educontrolservice.service.dao.file.FileInterface;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class FileService implements FileInterface {
    private final BankMaterialsClientFeign bankMaterialsClientFeign;

    public FileService(BankMaterialsClientFeign bankMaterialsClientFeign) {
        this.bankMaterialsClientFeign = bankMaterialsClientFeign;
    }

    @Override
    public Object uploadFile(List<MultipartFile> files, String descriptions, ResourceType resourceType, Long resourceId) {
        log.info("REQUEST (uploadFile) FROM [bankmaterials-service]");
        return bankMaterialsClientFeign.uploadFile(files, descriptions, resourceType, resourceId);

    }

    @Override
    public Resource getFileAsResource(Long id) {
        log.info("REQUEST (getFileAsResource) FROM [bankmaterials-service]");
        return bankMaterialsClientFeign.downloadFile(id);
    }

    @Override
    public Object getFileInfo(Long id) {
        log.info("REQUEST (getFileInfo) FROM [bankmaterials-service]");
        return bankMaterialsClientFeign.getFileInfo(id);
    }

    @Override
    public List<Object> getAllFilesInfo(Boolean status) {
        log.info("REQUEST (getAllFilesInfo) FROM [bankmaterials-service]");
        return bankMaterialsClientFeign.getAllFiles(status);
    }

    @Override
    public Object deleteFile(Long id) {
        log.info("REQUEST (deleteFile) FROM [bankmaterials-service]");
        return bankMaterialsClientFeign.deleteFile(id);
    }

    @Override
    public List<Object> getAllFilesInfoByDepartmentId(Long departmentId) {
        log.info("REQUEST (getAllFilesInfoByDepartmentId) FROM [bankmaterials-service]");
        return bankMaterialsClientFeign.getAllFilesByDepartment(departmentId);
    }

    @Override
    public Object updateFileInfo(Long id, UpdateFileUploadDTO request) {
        log.info("REQUEST (updateFileInfo) FROM [bankmaterials-service]");
        return bankMaterialsClientFeign.updateFileInfo(id, request);
    }

    @Override
    public ResponseEntity<StreamingResponseBody> streamVideo(Long videoId, Long start, Long end) {
        Response feignResponse = bankMaterialsClientFeign.streamVideo(videoId, start, end);
        Response.Body body = feignResponse.body();

        if (body == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        StreamingResponseBody stream = outputStream -> {
            try (InputStream inputStream = body.asInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                }
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Ranges", "bytes");

        feignResponse.headers().get("Content-Type").stream().findFirst()
                .ifPresent(type -> headers.set(HttpHeaders.CONTENT_TYPE, type));
        feignResponse.headers().get("Content-Length").stream().findFirst()
                .ifPresent(length -> headers.set(HttpHeaders.CONTENT_LENGTH, length));
        feignResponse.headers().get("Content-Range").stream().findFirst()
                .ifPresent(range -> headers.set(HttpHeaders.CONTENT_RANGE, range));

        HttpStatus status = feignResponse.status() == 206 ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;

        return new ResponseEntity<>(stream, headers, status);
    }

}
