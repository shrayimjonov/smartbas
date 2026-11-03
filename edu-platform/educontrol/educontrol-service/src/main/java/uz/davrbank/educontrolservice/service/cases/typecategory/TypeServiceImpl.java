package uz.davrbank.educontrolservice.service.cases.typecategory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.typecategory.TypeDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.TypeFilter;
import uz.davrbank.educontrolservice.feign.typecategory.TypeFeign;
import uz.davrbank.educontrolservice.service.dao.typecategory.TypeService;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

    TypeFeign typeFeign;

    @Override
    public Object create(TypeDTO typeDTO) {
        log.info("REQUEST (createType) FROM [assigment-service]");
        return typeFeign.create(typeDTO);
    }

    @Override
    public Object getList(TypeFilter filter) {
        log.info("REQUEST (getAllTypes) FROM [assigment-service]");
        return typeFeign.getList(filter);
    }

    @Override
    public Object getPageable(TypeFilter filter) {
        return typeFeign.getPageable(filter);
    }

    @Override
    public Object delete(Long id) {
        log.info("REQUEST (deleteType) FROM [assigment-service]");
        return typeFeign.delete(id);
    }

    @Override
    public Object update(Long id, TypeDTO typeDTO) {
        log.info("REQUEST (updateType) FROM [assigment-service] ");
        return typeFeign.update(id, typeDTO);
    }

    @Override
    public Object get(Long id) {
        log.info("REQUEST (getOneType) FROM [assigment-service]");
        return typeFeign.get(id);
    }
}