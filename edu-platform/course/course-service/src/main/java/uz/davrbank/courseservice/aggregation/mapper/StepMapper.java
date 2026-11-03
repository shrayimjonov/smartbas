package uz.davrbank.courseservice.aggregation.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.davrbank.courseservice.aggregation.dto.step.StepDTO;
import uz.davrbank.courseservice.aggregation.model.step.Step;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StepMapper {

    @Named("defaultMapping")
    @Mapping(target = "course", expression = "java(new uz.davrbank.courseservice.aggregation.model.course.Course(stepDTO.courseId()))")
    Step toEntity(StepDTO stepDTO);

    @IterableMapping(qualifiedByName = "defaultMapping")
    @Mapping(target = "course", expression = "java(new uz.davrbank.courseservice.aggregation.model.course.Course(stepDTO.courseId()))")
    List<Step> toEntityList(List<StepDTO> stepDTOList);

    @Mapping(target = "courseId", expression = "java(step.getCourse().getId())")
    StepDTO toDto(Step step);
}

