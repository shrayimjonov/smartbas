package uz.davrbank.educontrolservice.service.dao.department;

import uz.davrbank.educontrolservice.aggregate.dto.position.PositionRequestDTO;

import java.util.List;

public interface PositionInterface {

    Object createPosition(PositionRequestDTO positionRequestDTO);

    Object getPositionById(Long id);

    List<Object> getAllPositions();

    Object updatePosition(Long id, PositionRequestDTO positionRequestDTO);

    Object deletePosition(Long id);

}
