package uz.davrbank.courseservice.aggregation.dto.course;

import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;

import java.util.List;

public record AttachCourseResponseDTO(
        PageResponseDTO<UserCourseResponseDTO> userCourseResponseDTO
) {
}
