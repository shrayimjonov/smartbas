package uz.davrbank.educontrolservice.service.cases.typecategory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.typecategory.CategoryDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CategoryFilter;
import uz.davrbank.educontrolservice.feign.typecategory.CategoryFeign;
import uz.davrbank.educontrolservice.service.dao.typecategory.CategoryService;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {

    CategoryFeign categoryFeign;

    @Override
    public Object create(CategoryDTO categoryDTO) {
        log.info("REQUEST (createCategory) FROM [assigment-service]");
        return categoryFeign.create(categoryDTO);
    }

    @Override
    public Object getList(CategoryFilter filter) {
        log.info("REQUEST (getAllCategories) FROM [assigment-service]");
        return categoryFeign.getList(filter);
    }

    @Override
    public Object getPageable(CategoryFilter filter) {
        log.info("REQUEST (getAllCategories) FROM [assigment-service]");
        return categoryFeign.getPageable(filter);
    }

    @Override
    public Object delete(Long id) {
        log.info("REQUEST (deleteCategory) FROM [assigment-service]");
        return categoryFeign.delete(id);
    }

    @Override
    public Object update(Long id, CategoryDTO categoryDTO) {
        log.info("REQUEST (updateCategory) FROM [assigment-service] ");
        return categoryFeign.update(id, categoryDTO);
    }

    @Override
    public Object get(Long id) {
        log.info("REQUEST (getOneCategory) FROM [assigment-service]");
        return categoryFeign.get(id);
    }
}