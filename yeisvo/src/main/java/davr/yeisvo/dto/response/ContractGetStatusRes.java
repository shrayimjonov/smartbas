package davr.yeisvo.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for api/contract/status
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContractGetStatusRes extends BaseResponse {

    private Long gtkId;

    private Integer cntrStatus;


}
