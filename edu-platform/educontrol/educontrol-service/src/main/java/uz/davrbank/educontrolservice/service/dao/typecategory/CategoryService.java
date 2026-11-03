package uz.davrbank.educontrolservice.service.dao.typecategory;

import uz.davrbank.educontrolservice.aggregate.dto.assigment.typecategory.CategoryDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CategoryFilter;

public interface CategoryService {

    Object create(CategoryDTO categoryDTO);

    Object getList(CategoryFilter filter);

    Object getPageable(CategoryFilter filter);

    Object delete(Long id);

    Object update(Long id, CategoryDTO categoryDTO);

    Object get(Long id);
}
