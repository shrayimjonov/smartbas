package uz.davrbank.yeisvoservice.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMfoJuridicResponse implements Serializable {
    private String success;
    private String error;
    private long total;
    private List<ResponseInnDTO> data;
}
