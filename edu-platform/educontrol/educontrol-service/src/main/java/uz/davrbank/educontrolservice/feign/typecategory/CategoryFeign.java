package uz.davrbank.educontrolservice.feign.typecategory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.typecategory.CategoryDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CategoryFilter;
import uz.davrbank.educontrolservice.feign.config.AssignmentFeignServiceConfiguration;

@FeignClient(name = "assignment-service", contextId = "category", configuration = AssignmentFeignServiceConfiguration.class)
public interface CategoryFeign {

    @PostMapping("/assignment-service/api/v1/category")
    Object create(@RequestBody CategoryDTO categoryDTO);

    @GetMapping("/assignment-service/api/v1/category")
    Object getPageable(@SpringQueryMap CategoryFilter filter);

    @GetMapping("/assignment-service/api/v1/category/list")
    Object getList(@SpringQueryMap CategoryFilter filter);

    @DeleteMapping("/assignment-service/api/v1/category/{id}")
    Object delete(@PathVariable Long id);

    @PutMapping("/assignment-service/api/v1/category/{id}")
    Object update(@PathVariable Long id,
                  @RequestBody CategoryDTO categoryDTO);

    @GetMapping("/assignment-service/api/v1/category{id}")
    Object get(@PathVariable Long id);
}