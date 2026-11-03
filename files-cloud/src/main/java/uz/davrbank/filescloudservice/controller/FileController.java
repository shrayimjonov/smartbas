package uz.davrbank.filescloudservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.filescloudservice.aggrecation.dto.MonthFilesDTO;
import uz.davrbank.filescloudservice.aggrecation.enums.Applications;
import uz.davrbank.filescloudservice.service.interfaces.FileInterface;

import java.util.Map;

@RestController
@RequestMapping("/v1")
public class FileController {
    private final FileInterface fileInterface;

    public FileController(FileInterface fileInterface) {
        this.fileInterface = fileInterface;
    }

    @GetMapping("/all/files")
    public ResponseEntity<Map<String, Map<String, MonthFilesDTO>>> getAllFiles(
            @RequestParam("folder") Applications service
    ) {
        return ResponseEntity.ok(fileInterface.getAllFiles(service));
    }

    @GetMapping("/get/file")
    public ResponseEntity<?> getFileByPath(
            @RequestParam("path") String filePath
    ) {
        return fileInterface.getFile(filePath);
    }
}
