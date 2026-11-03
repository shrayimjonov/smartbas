package uz.davrbank.educontrolservice.service.cases.statistic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.feign.statistics.StatisticInterfaceFeignClient;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.statistic.StatisticInterface;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatisticService implements StatisticInterface {
    private final JwtTokenService jwtTokenService;
    private final StatisticInterfaceFeignClient feignClient;

    public StatisticService(JwtTokenService jwtTokenService, StatisticInterfaceFeignClient feignClient) {
        this.jwtTokenService = jwtTokenService;
        this.feignClient = feignClient;
    }

    @Override
    public List<Object> getPopularTasks() {
        log.info("REQUEST (getPopularTasks) FROM [statistic-service]");
        return feignClient.getPopularTasks();
    }

    @Override
    public List<Object> getDepartmentStats() {
        log.info("REQUEST (getDepartmentStats) FROM [statistic-service]");
        return feignClient.getDepartmentStatistics();
    }

    @Override
    public Map<String, Object> getOnlineUsers() {
        log.info("REQUEST (getOnlineUsers) FROM [statistic-service]");
        return feignClient.getOnlineUsersAndCount();
    }

    @Override
    public List<Object> getCompletedCourseCount() {
        log.info("REQUEST (getCompletedCourseCount) FROM [statistic-service]");
    return feignClient.getCompletedCourseCount();
    }

    @Override
    public List<Object> getCompletedStepCount(Long courseId) {
        log.info("REQUEST (getCompletedStepCount) FROM [statistic-service]");
        String employeeId = jwtTokenService.getCurrentUserId();
        return feignClient.getCompletedStepCount(employeeId, courseId);
    }

    @Override
    public Object getStepCompletionPercentage(String employeeId, Long courseId) {
        return null;
    }
}
