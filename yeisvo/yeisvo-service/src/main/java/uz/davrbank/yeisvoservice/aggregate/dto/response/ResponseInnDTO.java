package uz.davrbank.yeisvoservice.aggregate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.io.Serial;
import java.io.Serializable;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseInnDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("BRANCH")
    private String branch;

    @JsonProperty("CLIENT_ID")
    private String clientId;

    @JsonProperty("NIBBD_ID")
    private String nibbdId;

    @JsonProperty("INN")
    private String inn;

    @JsonProperty("NAME")
    private String name;

    @JsonProperty("STATE")
    private Integer state;
}

