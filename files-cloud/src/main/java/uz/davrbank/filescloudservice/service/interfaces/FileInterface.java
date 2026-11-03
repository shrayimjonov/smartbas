package uz.davrbank.filescloudservice.service.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import uz.davrbank.filescloudservice.aggrecation.dto.MonthFilesDTO;
import uz.davrbank.filescloudservice.aggrecation.enums.Applications;

import java.util.Map;


public interface FileInterface {
    Map<String, Map<String, MonthFilesDTO>> getAllFiles(Applications service);
    ResponseEntity<Resource> getFile(String filePath);
}
