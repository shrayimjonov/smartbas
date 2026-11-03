package uz.davrbank.educontrolservice.aggregate.dto.examinations.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExaminationRequestDto {
    @JsonProperty("examinationTitle")
    private String examinationTitle;

    @JsonProperty("startTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime; // Global Time (Front fix)

    @JsonProperty("endTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @JsonProperty("type")
    private TypeGeneral type; // course or library

    @JsonProperty("status")
    private StatusCondition status;

    @JsonProperty("courseId")
    private Long courseId; // Course id  by default 1L

    @JsonProperty("cloudresourceId")
    private Long cloudresourceId; // Cloud Resource id by default 1L
}
