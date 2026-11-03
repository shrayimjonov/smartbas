package uz.davrbank.educontrolservice.aggregate.dto.filter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseFilter extends BaseFilter {

    Long categoryId;
    Long departmentId;
    Boolean isPublic;
    Boolean deleted;
    Integer timeCompleteMin;
    Integer timeCompleteMax;
    Double scoreMin;
    Double scoreMax;
    String employeeId;
    List<String> roles;
}
