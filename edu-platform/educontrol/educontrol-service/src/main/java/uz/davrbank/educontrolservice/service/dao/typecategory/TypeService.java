package uz.davrbank.educontrolservice.service.dao.typecategory;

import uz.davrbank.educontrolservice.aggregate.dto.assigment.typecategory.TypeDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.TypeFilter;

public interface TypeService {

    Object create(TypeDTO typeDTO);

    Object getList(TypeFilter filter);

    Object getPageable(TypeFilter filter);

    Object delete(Long id);

    Object update(Long id, TypeDTO typeDTO);

    Object get(Long id);
}
