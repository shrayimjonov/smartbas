package uz.davrbank.yeisvoservice.aggregate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JuridicClientRequest {

    private String bankId;
    private int active;
}
