package uz.davrbank.educontrolservice.service.dao.file;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import uz.davrbank.educontrolservice.aggregate.dto.file.ResourceType;
import uz.davrbank.educontrolservice.aggregate.dto.file.UpdateFileUploadDTO;

import java.util.List;

public interface FileInterface {

    Object uploadFile(List<MultipartFile> files, String descriptions, ResourceType resourceType, Long resourceId);

    Resource getFileAsResource(Long id);

    Object getFileInfo(Long id);

    List<Object> getAllFilesInfo(Boolean status);

    Object deleteFile(Long id);

    Object getAllFilesInfoByDepartmentId(Long departmentId);

    Object updateFileInfo(Long id, UpdateFileUploadDTO request);

    ResponseEntity<StreamingResponseBody> streamVideo(Long videoId, Long start, Long end);
}
