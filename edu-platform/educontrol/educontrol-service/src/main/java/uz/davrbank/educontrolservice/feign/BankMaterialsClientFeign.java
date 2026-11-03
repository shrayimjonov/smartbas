package uz.davrbank.educontrolservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import uz.davrbank.educontrolservice.aggregate.dto.file.ResourceType;
import uz.davrbank.educontrolservice.aggregate.dto.file.UpdateFileUploadDTO;
import uz.davrbank.educontrolservice.feign.config.BankMaterialsFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "bankmaterials-service", configuration = BankMaterialsFeignServiceConfiguration.class)
public interface BankMaterialsClientFeign {

    @PostMapping(value = "/api/v1/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Object uploadFile(@RequestPart("files") List<MultipartFile> files,
                      @RequestPart(value = "descriptions") String descriptions,
                      @RequestParam("resourceType") ResourceType resourceType,
                      @RequestParam(value = "resourceId", required = false, defaultValue = "0") Long resourceId);

    @GetMapping("/api/v1/file/info/{id}")
    Object getFileInfo(@PathVariable Long id);

    @GetMapping(value = "/api/v1/file/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    Resource downloadFile(@PathVariable Long id);

    @GetMapping("/api/v1/file/get/all")
    List<Object> getAllFiles(@RequestParam(value = "status", required = false) Boolean status);

    @DeleteMapping("/api/v1/file/delete/{id}")
    Object deleteFile(@PathVariable Long id);

    @GetMapping("/api/v1/file/get/all/department/{id}")
    List<Object> getAllFilesByDepartment(@PathVariable Long id);

    @PutMapping("/api/v1/file/update/{id}")
    Object updateFileInfo(@PathVariable Long id,
                          @RequestBody UpdateFileUploadDTO request);


    @GetMapping(value = "/api/videos/{videoId}", consumes = MediaType.ALL_VALUE)
    feign.Response streamVideo(@PathVariable Long videoId,
                                      @RequestParam Long start,
                                      @RequestParam Long end);
}
