package uz.davrbank.courseservice.service.cases.course;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.davrbank.courseservice.aggregation.dto.step.FileUploadDTO;
import uz.davrbank.courseservice.aggregation.enums.ResourceType;
import uz.davrbank.courseservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.courseservice.aggregation.mapper.FileMapper;
import uz.davrbank.courseservice.aggregation.model.FileUpload;
import uz.davrbank.courseservice.repository.FilesRepository;
import uz.davrbank.courseservice.service.dao.FileUploadService;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    FileMapper fileMapper;
    FilesRepository repository;

    @Override
    public List<FileUploadDTO> getAllFilesInfo(Long resourceId, ResourceType resourceType) {
        try {
            LOGGER.info("Запрос на получение списка файлов");
            List<FileUpload> fileUploads = repository.findAllByResourceIdAndResourceTypeAndIsActive(resourceId, resourceType, true);
            LOGGER.info("Успешно получено {} файлов", fileUploads.size());
            return fileMapper.toDtoList(fileUploads);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException("Ошибка при получении списка файлов", e);
        }
    }

    @Override
    public void deleteFileByResourceId(Long resourceId, ResourceType resourceType) {
        try {
            LOGGER.info("Запрос на удаление файлов по id шага: {}", resourceId);
            List<FileUpload> fileUploads = repository.findAllByResourceIdAndResourceTypeAndIsActive(resourceId, resourceType, true);
            fileUploads.forEach(fileUpload -> {
                fileUpload.setIsActive(false);
                repository.save(fileUpload);
            });
            LOGGER.info("Файлы успешно удалены");
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException("Файлы не найдены", e);
        }
    }
}
