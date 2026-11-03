package uz.davrbank.educontrolservice.feign.department;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.position.PositionRequestDTO;
import uz.davrbank.educontrolservice.feign.config.DepartmentFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "department-service", contextId = "position", configuration = DepartmentFeignServiceConfiguration.class)
public interface PositionClientFeign {

    @PostMapping("/api/v1/position/create")
    Object createPosition(@RequestBody PositionRequestDTO positionRequestDTO);

    @PutMapping("/api/v1/position/update/{id}")
    Object updatePosition(@PathVariable Long id, @RequestBody PositionRequestDTO positionRequestDTO);

    @GetMapping("/api/v1/position/get/all")
    List<Object> getAllPositions();

    @GetMapping("/api/v1/position/get/{id}")
    Object getPositionById(@PathVariable Long id);

    @DeleteMapping("/api/v1/position/delete/{id}")
    Object deletePosition(@PathVariable Long id);
}
