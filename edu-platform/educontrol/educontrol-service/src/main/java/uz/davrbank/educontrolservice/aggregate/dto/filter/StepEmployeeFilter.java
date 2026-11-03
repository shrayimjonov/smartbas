package uz.davrbank.educontrolservice.aggregate.dto.filter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.educontrolservice.aggregate.enums.Status;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepEmployeeFilter extends BaseFilter {

    Long stepId;
    String employeeId;
    Boolean isCompleted;
    List<Status> statuses;
}
