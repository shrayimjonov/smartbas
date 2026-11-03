package uz.davrbank.assignmentservice.service.cases.course;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.assignmentservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.assignmentservice.aggregation.dto.general.TypeRequestDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.TypeResponseDTO;
import uz.davrbank.assignmentservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.assignmentservice.aggregation.filter.TypeFilter;
import uz.davrbank.assignmentservice.aggregation.model.course.Type;
import uz.davrbank.assignmentservice.repository.TypeRepository;
import uz.davrbank.assignmentservice.repository.strategy.SpecificationApplyFilterStrategy;
import uz.davrbank.assignmentservice.service.dao.TypeService;

import java.util.List;

@Service
@Transactional
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class TypeServiceImpl implements TypeService {
    TypeRepository typeRepository;
    SpecificationApplyFilterStrategy<Type, TypeFilter> strategy;

    @Override
    public ResponseSuccess create(TypeRequestDTO typeRequestDTO) {
        log.info("Создание типа категории: {}", typeRequestDTO);
        try {
            Type type = new Type();
            type.setName(typeRequestDTO.name());
            typeRepository.save(type);
            return new ResponseSuccess("Тип категории успешно создан");
        } catch (Exception e) {
            log.error("Ошибка при создании типа категории: {}", e.getMessage());
            throw new RuntimeException("Ошибка при создании типа категории" + e.getMessage());
        }
    }

    @Override
    public ResponseSuccess update(Long id, TypeRequestDTO typeRequestDTO) {
        log.info("Обновление типа категории с id: {}", id);
        try {
            Type type = typeRepository.findById(id).orElseThrow(() ->
                    new DataNotFoundException("Тип категории не найден"));
            if (typeRequestDTO.name() != null) {
                type.setName(typeRequestDTO.name());
            }
            typeRepository.save(type);
            return new ResponseSuccess("Тип категории успешно обновлен");
        } catch (DataNotFoundException e) {
            log.error("Ошибка при обновлении типа категории: {}", e.getMessage());
            throw new DataNotFoundException("Ошибка при обновлении типа категории: ", e);
        }
    }

    @Override
    public ResponseSuccess delete(Long id) {
        log.info("Удаление типа категории с id: {}", id);
        try {
            Type type = typeRepository.findById(id).orElseThrow(() ->
                    new DataNotFoundException("Тип категории не найден"));
            typeRepository.delete(type);
            return new ResponseSuccess("Тип категории успешно удален");
        } catch (DataNotFoundException e) {
            log.error("Ошибка при удалении типа категории: {}", e.getMessage());
            throw new DataNotFoundException("Ошибка при удал��нии типа категории: " + e.getMessage());
        }
    }

    @Override
    public TypeResponseDTO get(Long id) {
        log.info("Получение типа категории с id: {}", id);
        try {
            Type type = typeRepository.findById(id).orElseThrow(() ->
                    new DataNotFoundException("Тип категории не найден"));
            return new TypeResponseDTO(type.getId(), type.getName());
        } catch (DataNotFoundException e) {
            log.error("Ошибка при получении типа категории: {}", e.getMessage());
            throw new DataNotFoundException("Ошибка при получении типа категории: " + e.getMessage());
        }
    }

    @Override
    public List<TypeResponseDTO> getList(TypeFilter filter) {
        log.info("Получение всех типов категорий");
        List<Type> list = typeRepository.findAll(strategy.applyFilter(filter));
        return list.stream()
                .map(type -> new TypeResponseDTO(type.getId(), type.getName()))
                .toList();
    }

    @Override
    public PageResponseDTO<TypeResponseDTO> getPageable(TypeFilter filter) {
        Page<Type> page = typeRepository.findAll(strategy.applyFilter(filter), strategy.buildPage(filter));
        List<TypeResponseDTO> content = page.getContent().stream()
                .map(type -> new TypeResponseDTO(type.getId(), type.getName()))
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
