package uz.devops.humoMiddleware.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.basic.Customer;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetClientsResponseDTO {

    @JsonProperty("Customer")
    private List<Customer> customer;
    private String customerId;
    private String bankId;

}
