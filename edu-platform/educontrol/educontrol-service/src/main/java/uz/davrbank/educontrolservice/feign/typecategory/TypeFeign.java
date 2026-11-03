package uz.davrbank.educontrolservice.feign.typecategory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.typecategory.TypeDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.TypeFilter;
import uz.davrbank.educontrolservice.feign.config.AssignmentFeignServiceConfiguration;

@FeignClient(name = "assignment-service", contextId = "type", configuration = AssignmentFeignServiceConfiguration.class)
public interface TypeFeign {

    @PostMapping("assignment-service/api/v1/type")
    Object create(@RequestBody TypeDTO typeDTO);

    @GetMapping("assignment-service/api/v1/type/list")
    Object getList(@SpringQueryMap TypeFilter filter);

    @GetMapping("assignment-service/api/v1/type")
    Object getPageable(@SpringQueryMap TypeFilter filter);

    @DeleteMapping("assignment-service/api/v1/type/{id}")
    Object delete(@PathVariable Long id);

    @PutMapping("assignment-service/api/v1/type/{id}")
    Object update(@PathVariable Long id,
                  @RequestBody TypeDTO typeDTO);

    @GetMapping("assignment-service/api/v1/type/{id}")
    Object get(@PathVariable Long id);
}