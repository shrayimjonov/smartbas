package uz.davrbank.courseservice.aggregation.filter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.courseservice.aggregation.enums.Status;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseEmployeeFilter extends BaseFilter {

    Long courseId;
    String employeeId;
    Boolean isCompleted;
    List<Status> statuses;
}
