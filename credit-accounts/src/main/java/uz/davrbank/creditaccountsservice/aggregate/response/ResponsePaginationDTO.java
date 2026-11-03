package uz.davrbank.creditaccountsservice.aggregate.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePaginationDTO extends ResponseCoreDTO implements Serializable {
    private Integer totalPages;
    private Boolean last;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer numberOfElements;
    private Boolean first;
    private Boolean empty;
    private List<CreditAccountResDataDTO> data;
}
