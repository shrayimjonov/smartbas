package davr.yeisvo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankBranchResponse extends BaseResponse {
    private Long id;
    private String inn;
    private String mfo;
    private String name;
    private Boolean isActive;
}
