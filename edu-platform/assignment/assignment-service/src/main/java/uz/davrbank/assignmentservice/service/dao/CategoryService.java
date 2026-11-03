package uz.davrbank.assignmentservice.service.dao;

import uz.davrbank.assignmentservice.aggregation.dto.course.CategoryRequestDTO;
import uz.davrbank.assignmentservice.aggregation.dto.course.CategoryResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.assignmentservice.aggregation.filter.CategoryFilter;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO);

    ResponseSuccess update(Long id, CategoryRequestDTO categoryRequestDTO);

    CategoryResponseDTO get(Long id);

    ResponseSuccess delete(Long id);

    List<CategoryResponseDTO> getList(CategoryFilter filter);

    PageResponseDTO<CategoryResponseDTO> getPageable(CategoryFilter filter);

}
