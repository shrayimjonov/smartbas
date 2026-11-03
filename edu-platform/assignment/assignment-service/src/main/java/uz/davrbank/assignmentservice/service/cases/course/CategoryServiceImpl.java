package uz.davrbank.assignmentservice.service.cases.course;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uz.davrbank.assignmentservice.aggregation.dto.course.CategoryRequestDTO;
import uz.davrbank.assignmentservice.aggregation.dto.course.CategoryResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.assignmentservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.assignmentservice.aggregation.filter.CategoryFilter;
import uz.davrbank.assignmentservice.aggregation.mapper.CategoryMapper;
import uz.davrbank.assignmentservice.aggregation.model.course.Category;
import uz.davrbank.assignmentservice.repository.CategoryRepository;
import uz.davrbank.assignmentservice.repository.strategy.SpecificationApplyFilterStrategy;
import uz.davrbank.assignmentservice.service.dao.CategoryService;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    static Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    SpecificationApplyFilterStrategy<Category, CategoryFilter> strategy;

    @Override
    public CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO) {
        LOGGER.info("Создание категории: {}", categoryRequestDTO);
        try {
            Category category = categoryMapper.toEntityFromReq(categoryRequestDTO);
            return categoryMapper.toDto(categoryRepository.save(category));
        } catch (Exception e) {
            LOGGER.error("Ошибка при создании категории: {}", e.getMessage());
            throw new RuntimeException("Ошибка при создании категории: " + e.getMessage());
        }
    }

    @Override
    public ResponseSuccess update(Long id, CategoryRequestDTO categoryRequestDTO) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Категория не найдена"));
            categoryMapper.updateFromReqDto(categoryRequestDTO, category);
            categoryRepository.save(category);
            return new ResponseSuccess("Категория успешно обновлена");
        } catch (Exception e) {
            LOGGER.error("Ошибка при обновлении категории: {}", e.getMessage());
            throw new DataNotFoundException("Ошибка при обновлении категории: " + e.getMessage());
        }
    }

    @Override
    public CategoryResponseDTO get(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Категория не найдена"));
            return categoryMapper.toDto(category);
        } catch (Exception e) {
            LOGGER.error("Ошибка при получении категории: {}", e.getMessage());
            throw new DataNotFoundException("Ошибка при получении категории: " + e.getMessage());

        }
    }

    @Override
    public ResponseSuccess delete(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            categoryRepository.delete(category);
            return new ResponseSuccess("Категория успешно удалена");
        } catch (Exception e) {
            LOGGER.error("Ошибка при удалении категории: {}", e.getMessage());
            throw new DataNotFoundException("Error while deleting category: " + e.getMessage());
        }
    }

    @Override
    public List<CategoryResponseDTO> getList(CategoryFilter filter) {
        LOGGER.info("Получение всех категорий");
        List<Category> list = categoryRepository.findAll(strategy.applyFilter(filter));
        return categoryMapper.toDtoList(list);
    }

    @Override
    public PageResponseDTO<CategoryResponseDTO> getPageable(CategoryFilter filter) {
        Page<Category> page = categoryRepository.findAll(strategy.applyFilter(filter), strategy.buildPage(filter));
        List<CategoryResponseDTO> content = page.getContent().stream()
                .map(categoryMapper::toDto)
                .toList();
        return new PageResponseDTO<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }
}
