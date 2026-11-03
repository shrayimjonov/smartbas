package uz.davrbank.educontrolservice.aggregate.dto.examinations.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamCreationRequestDto {

    @JsonProperty("examinationTitle")
    @NotNull(message = "examination title is required")
    private String examinationTitle;

    @JsonProperty("startTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "examination title is required")
    private LocalDateTime startTime; // Global Time (Front fix)

    @JsonProperty("endTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "examination t is required")
    private LocalDateTime endTime;

    @JsonProperty("courseId")
    private Long courseId; // Course id  by default 1L

    @JsonProperty("cloudresourceId")
    private Long cloudresourceId; // Cloud Resource id by default 1L

    private Long departmentId;          // Scenario 1: Assign to everyone in this Dept
    private List<String> employeeIds;   // Scenario 2: Assign to specific UUIDs
}
