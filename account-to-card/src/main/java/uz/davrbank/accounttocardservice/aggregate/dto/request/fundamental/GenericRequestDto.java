package uz.davrbank.accounttocardservice.aggregate.dto.request.fundamental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenericRequestDto<T> {
     private Integer id;
     private String jsonrpc;
     private String method;
     private T params;
}