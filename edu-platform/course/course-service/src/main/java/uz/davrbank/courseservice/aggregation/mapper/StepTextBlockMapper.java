package uz.davrbank.courseservice.aggregation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.davrbank.courseservice.aggregation.dto.step.StepTextBlockRequestDTO;
import uz.davrbank.courseservice.aggregation.dto.step.StepTextBlockResponseDTO;
import uz.davrbank.courseservice.aggregation.model.step.StepTextBlock;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StepTextBlockMapper extends EntityMapping<StepTextBlock, StepTextBlockRequestDTO, StepTextBlockResponseDTO> {


    @Override
    @Mapping(target = "stepId", source = "step.id")
    StepTextBlockResponseDTO toDto(StepTextBlock entity);

    @Override
    @Mapping(target = "stepId", source = "step.id")
    List<StepTextBlockResponseDTO> toDtoList(List<StepTextBlock> entityList);
}
