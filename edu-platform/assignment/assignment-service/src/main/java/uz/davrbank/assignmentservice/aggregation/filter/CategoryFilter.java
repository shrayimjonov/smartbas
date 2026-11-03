package uz.davrbank.assignmentservice.aggregation.filter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryFilter extends BaseFilter {

    Long typeId;
}
