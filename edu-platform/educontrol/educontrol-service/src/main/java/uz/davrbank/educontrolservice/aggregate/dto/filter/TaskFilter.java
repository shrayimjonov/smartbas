package uz.davrbank.educontrolservice.aggregate.dto.filter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskFilter extends BaseFilter {

    Long typeId;
}
