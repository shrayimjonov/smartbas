package davr.yeisvo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * request DTO for contract list api
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContractListRequestBody {
    private String bankInn;
    private String bankId;
    private String contractorUzInn;

    //search uchun fieldlar
    private String idn;
    private String docNo;
    private String contractorUzName;
    private int size;
    private int page;

    @JsonProperty("isUpdate")
    private boolean isUpdate =  false; // agar true kelsa update qilib ken qaytaramiz
}
