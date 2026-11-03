package uz.davrbank.educontrolservice.service.dao.statistic;


import java.util.List;
import java.util.Map;

public interface StatisticInterface {

    List<Object> getPopularTasks();

    List<Object> getDepartmentStats();

    Map<String, Object> getOnlineUsers();

    List<Object> getCompletedCourseCount();

    List<Object> getCompletedStepCount(Long courseId);

    Object getStepCompletionPercentage(String employeeId, Long courseId);

}
