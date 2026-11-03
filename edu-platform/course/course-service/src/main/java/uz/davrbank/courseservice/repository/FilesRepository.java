package uz.davrbank.courseservice.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.courseservice.aggregation.enums.ResourceType;
import uz.davrbank.courseservice.aggregation.model.FileUpload;

import java.util.List;


@Repository
public interface FilesRepository extends JpaRepository<FileUpload, Long> {

    FileUpload findByIdAndIsActive(Long id, boolean active);

    List<FileUpload> findAllByResourceIdAndResourceTypeAndIsActive(@NotNull Long stepId, ResourceType resourceType, boolean active);
}