package uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsUserDto {
    @JsonProperty("EMP_ID")
    private Long empId;

    @JsonProperty("BRANCH")
    private String branch;

    @JsonProperty("USER_NAME")
    private String userName;

    @JsonProperty("FULL_NAME")
    private String fullName;

    @JsonProperty("TITLE")
    private String title;
}
