package uz.davrbank.courseservice.aggregation.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.davrbank.courseservice.aggregation.dto.course.CourseRequestDTO;
import uz.davrbank.courseservice.aggregation.dto.course.CourseResponseDTO;
import uz.davrbank.courseservice.aggregation.model.course.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper extends EntityMapping<Course, CourseRequestDTO, CourseResponseDTO> {
}
