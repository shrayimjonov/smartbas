package uz.davrbank.bustreasury.aggregate.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OperationUserRequestDTO {

    @JsonProperty("TEMPLATE_ID")
    private String templateId;

    @JsonProperty("SUMMA")
    private String summa;

    @JsonProperty("COURSE")
    private String course;

    @JsonProperty("CREATED_BY")
    private String createdBy;
}
