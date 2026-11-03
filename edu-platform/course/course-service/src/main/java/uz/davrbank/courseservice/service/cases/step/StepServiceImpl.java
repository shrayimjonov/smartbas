package uz.davrbank.courseservice.service.cases.step;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.courseservice.aggregation.dto.step.FileUploadDTO;
import uz.davrbank.courseservice.aggregation.dto.step.StepDTO;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.aggregation.enums.ResourceType;
import uz.davrbank.courseservice.aggregation.exception.condition.BadRequestException;
import uz.davrbank.courseservice.aggregation.exception.condition.ConnectionException;
import uz.davrbank.courseservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.courseservice.aggregation.model.step.Step;
import uz.davrbank.courseservice.aggregation.mapper.StepMapper;
import uz.davrbank.courseservice.repository.StepRepository;
import uz.davrbank.courseservice.service.dao.FileUploadService;
import uz.davrbank.courseservice.service.dao.step.StepService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StepServiceImpl implements StepService {

    private final static Logger log = LoggerFactory.getLogger(StepServiceImpl.class);
    StepMapper stepMapper;
    StepRepository repository;
    FileUploadService fileUploadService;

    @Override
    @Transactional
    public ResponseSuccess create(StepDTO stepDTO) {
        log.info("Request to createStep");
        try {
            Step step = stepMapper.toEntity(stepDTO);
            Step savedStep = repository.save(step);
            log.info(" step created successfully with ID: {}", savedStep.getId());
            return new ResponseSuccess("Step successfully created");
        } catch (Exception e) {
            log.error("Error creating  step: {}", e.getMessage(), e);
            throw new BadRequestException("Error while creating step");
        }
    }

    @Override
    @Transactional
    public ResponseSuccess update(StepDTO stepDTO, Long id) {
        log.info("Updating step with ID: {}", id);
        try {
            Optional<Step> optionalStep = repository.findById(id);
            if (optionalStep.isEmpty()) {
                log.warn("No  step found with ID: {}", id);
                return new ResponseSuccess("Step not found");
            }

            Step updatedStep = stepMapper.toEntity(stepDTO);
            updatedStep.setId(id);
            repository.save(updatedStep);
            log.info(" step updated successfully with ID: {}", id);
            return new ResponseSuccess("Step successfully updated");
        } catch (Exception e) {
            log.error("Error updating  step: {}", e.getMessage(), e);
            throw new BadRequestException("Error while updating step");
        }
    }

    @Override
    public List<StepDTO> getAllByCourseId(Long courseId) {
        log.info("Retrieving  step with course ID: {}", courseId);
        try {
            List<Step> steps = repository.findAllByCourse_IdAndIsActive(courseId, true);
            List<StepDTO> result = steps.stream()
                    .map(stepMapper::toDto)
                    .collect(Collectors.toList());
            log.info("Found {} steps for course ID: {}", result.size(), courseId);
            return result;
        } catch (Exception e) {
            log.error("Error fetching steps for course ID {}: {}", courseId, e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    @Transactional
    public ResponseSuccess delete(Long id) {
        log.info("Deleting  step with ID: {}", id);
        try {
            if (!repository.existsById(id)) {
                log.warn("Cannot delete step. No step found with ID: {}", id);
                return new ResponseSuccess("Step not found");
            }
            fileUploadService.deleteFileByResourceId(id, ResourceType.STEP);
            repository.updateIsActive(id, false);
            log.info(" step deleted with ID: {}", id);
            return new ResponseSuccess("Step successfully deleted");
        } catch (Exception e) {
            log.error("Error deleting step with ID {}: {}", id, e.getMessage(), e);
            throw new BadRequestException("Error while deleting step");
        }
    }

    @Override
    public StepDTO get(Long id) {
        log.info("Retrieving  step with ID: {}", id);
        try {
            Step step = repository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Step not found"));
            log.info("Fetched  step with ID: {}", id);
            return stepMapper.toDto(step);
        } catch (DataNotFoundException e) {
            log.warn(" step not found for ID: {}", id);
            throw new DataNotFoundException("Step not found");
        } catch (Exception e) {
            log.error("Error fetching step by ID {}: {}", id, e.getMessage(), e);
            throw new ConnectionException("Error while fetching step");
        }
    }

    @Override
    public List<FileUploadDTO> getAllFiles(Long stepId) {
        return fileUploadService.getAllFilesInfo(stepId, ResourceType.STEP);
    }
}
