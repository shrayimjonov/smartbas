package uz.davrbank.assignmentservice.service.dao;


import uz.davrbank.assignmentservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.assignmentservice.aggregation.dto.general.TypeRequestDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.TypeResponseDTO;
import uz.davrbank.assignmentservice.aggregation.filter.TypeFilter;

import java.util.List;

public interface TypeService {

    ResponseSuccess create(TypeRequestDTO typeRequestDTO);

    ResponseSuccess update(Long id, TypeRequestDTO typeRequestDTO);

    ResponseSuccess delete(Long id);

    TypeResponseDTO get(Long id);

    List<TypeResponseDTO> getList(TypeFilter filter);

    PageResponseDTO<TypeResponseDTO> getPageable(TypeFilter filter);
}
