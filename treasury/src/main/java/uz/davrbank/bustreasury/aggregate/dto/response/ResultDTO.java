package uz.davrbank.bustreasury.aggregate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO {
    List<ResponseDTO> responses;
}