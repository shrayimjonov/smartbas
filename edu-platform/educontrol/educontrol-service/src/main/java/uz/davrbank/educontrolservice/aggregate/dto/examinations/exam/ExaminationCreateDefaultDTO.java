package uz.davrbank.educontrolservice.aggregate.dto.examinations.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @project: eduexam-service
 * DTO for creating examinations with default values.
 * This DTO requires only essential fields - title, start time, and end time.
 *
 * Default values will be applied for:
 * - courseId: 1L
 * - type: TypeGeneral. COURSE
 * - status: StatusCondition.NOT_STARTED
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExaminationCreateDefaultDTO {

    @JsonProperty("examinationTitle")
    private String examinationTitle;

    @JsonProperty("startTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

}

