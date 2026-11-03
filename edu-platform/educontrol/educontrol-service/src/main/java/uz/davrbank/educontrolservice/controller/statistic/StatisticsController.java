package uz.davrbank.educontrolservice.controller.statistic;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.statistic.StatisticInterface;

@RestController
@RequestMapping("/api/v1/educontrol/statistic")
public class StatisticsController {

    private final StatisticInterface statisticInterface;


    public StatisticsController(StatisticInterface statisticInterface) {
        this.statisticInterface = statisticInterface;
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/popular/task")
    public ResponseEntity<ApiResponse<Object>> getPopularTask() {
        return ResponseEntity.ok(ApiResponse.of("Популярные задачи получены",
                statisticInterface.getPopularTasks()));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/department/stats")
    public ResponseEntity<ApiResponse<Object>> getDepartmentStats() {
        return ResponseEntity.ok(ApiResponse.of("Статистика департаментов получена",
                statisticInterface.getDepartmentStats()));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/online")
    public ResponseEntity<ApiResponse<Object>> getOnline() {
        return ResponseEntity.ok(ApiResponse.of("Список онлайн пользователей получен",
                statisticInterface.getOnlineUsers()));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/completed/course/count")
    public ResponseEntity<ApiResponse<Object>> getCompletedCourseCount() {
        return ResponseEntity.ok(ApiResponse.of("Количество завершённых курсов получено",
                statisticInterface.getCompletedCourseCount()));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/completed/step/count")
    public ResponseEntity<ApiResponse<Object>> getCompletedStepCount(@RequestParam Long courseId) {
        return ResponseEntity.ok(ApiResponse.of("Количество завершённых шагов получено",
                statisticInterface.getCompletedStepCount(courseId)));
    }
}
