package uz.davrbank.assignmentservice.aggregation.mapper;

import org.mapstruct.Mapper;
import uz.davrbank.assignmentservice.aggregation.dto.course.CategoryRequestDTO;
import uz.davrbank.assignmentservice.aggregation.dto.course.CategoryResponseDTO;
import uz.davrbank.assignmentservice.aggregation.model.course.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapping<Category, CategoryRequestDTO, CategoryResponseDTO> {
}
