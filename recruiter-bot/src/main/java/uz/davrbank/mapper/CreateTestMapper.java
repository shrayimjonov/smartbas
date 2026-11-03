package uz.davrbank.mapper;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import uz.davrbank.dto.request.CreateTestRequest;
import uz.davrbank.dto.response.CreateTestResponse;
import uz.davrbank.entity.CreateTest;

@Mapper(componentModel = "spring")
public interface CreateTestMapper {

    CreateTest toEntity(CreateTestRequest dto);

    CreateTestResponse toResponse(CreateTest dto);

    void updateEntity(@Valid CreateTestRequest dto, @MappingTarget CreateTest existingTest);
}
