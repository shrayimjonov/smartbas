package uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsListResponse<T> {
    private String status;
    private String error;
    private List<T> data = new ArrayList<>();
}
