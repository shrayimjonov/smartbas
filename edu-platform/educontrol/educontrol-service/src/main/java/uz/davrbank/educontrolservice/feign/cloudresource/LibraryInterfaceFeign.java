package uz.davrbank.educontrolservice.feign.cloudresource;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.library.LibraryRequestDTO;
import uz.davrbank.educontrolservice.feign.config.AssignmentFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "cloudresource-service", contextId = "library", configuration = AssignmentFeignServiceConfiguration.class)
public interface LibraryInterfaceFeign {

    @PostMapping("/api/v1/libraries/create")
    Object createLibrary(@RequestBody LibraryRequestDTO libraryRequestDTO);

    @PutMapping("/api/v1/libraries/update/{id}")
    Object updateLibrary(@PathVariable Long id,
                         @RequestBody LibraryRequestDTO libraryRequestDTO);

    @GetMapping("/api/v1/libraries/get/all")
    Object getAllLibraries(@RequestParam(required = false) String categoryId,
                                 @RequestParam(required = false) List<Long> departmentIds,
                                 @RequestParam int page,
                                 @RequestParam int size);

    @GetMapping("/api/v1/libraries/get/one/{id}")
    Object getOneLibraryData(@PathVariable Long id);

    @GetMapping("/api/v1/libraries/get/data/{id}")
    List<Object> getLibraryData(@PathVariable Long id);

    @DeleteMapping("/api/v1/libraries/delete/{id}")
    Object deleteLibrary(@PathVariable Long id);
}
