package uz.davrbank.educontrolservice.feign.statistics;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.davrbank.educontrolservice.feign.config.BankMaterialsFeignServiceConfiguration;
import uz.davrbank.educontrolservice.feign.config.StatisticFeignServiceConfiguration;

import java.util.List;
import java.util.Map;

@FeignClient(name = "statistic-service", contextId = "statistic", configuration = StatisticFeignServiceConfiguration.class)
public interface StatisticInterfaceFeignClient {

    @GetMapping("/api/v1/statistic/get/popular")
    List<Object> getPopularTasks();

    @GetMapping("/api/v1/statistic/get/department/stats")
    List<Object> getDepartmentStatistics();

    @GetMapping("/api/v1/statistic/get/online")
    Map<String, Object> getOnlineUsersAndCount();

    @GetMapping("/api/v1/statistic/get/completed/course/count")
    List<Object> getCompletedCourseCount();

    @GetMapping("/api/v1/statistic/get/completed/step/count")
    List<Object> getCompletedStepCount(@RequestParam String employeeId,
                                 @RequestParam Long courseId);
}
