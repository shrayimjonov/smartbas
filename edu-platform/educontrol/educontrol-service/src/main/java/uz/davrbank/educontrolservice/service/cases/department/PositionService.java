package uz.davrbank.educontrolservice.service.cases.department;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.position.PositionRequestDTO;
import uz.davrbank.educontrolservice.feign.department.PositionClientFeign;
import uz.davrbank.educontrolservice.service.dao.department.PositionInterface;

import java.util.List;

@Service
@Slf4j
public class PositionService implements PositionInterface {
    private final PositionClientFeign positionClientFeign;

    public PositionService(PositionClientFeign positionClientFeign) {
        this.positionClientFeign = positionClientFeign;
    }

    @Override
    public Object createPosition(PositionRequestDTO positionRequestDTO) {
        log.info("REQUEST (createPosition) FROM [department-service]");
        return positionClientFeign.createPosition(positionRequestDTO);
    }

    @Override
    public Object getPositionById(Long id) {
        log.info("REQUEST (getPositionById) FROM [department-service]");
        return positionClientFeign.getPositionById(id);
    }

    @Override
    public List<Object> getAllPositions() {
        log.info("REQUEST (getAllPositions) FROM [department-service]");
        return positionClientFeign.getAllPositions();
    }

    @Override
    public Object updatePosition(Long id, PositionRequestDTO positionRequestDTO) {
        log.info("REQUEST (updatePosition) FROM [department-service]");
        return positionClientFeign.updatePosition(id, positionRequestDTO);
    }

    @Override
    public Object deletePosition(Long id) {
        log.info("REQUEST (deletePosition) FROM [department-service]");
        return positionClientFeign.deletePosition(id);
    }
}
