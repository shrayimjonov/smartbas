package uz.davrbank.esudservice.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import uz.davrbank.esudservice.dto.out.ResponseData;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class RegisterCaseResponseDto extends BaseResponse<ResponseData> {

    @AssertTrue(message = "If claimId is present, message and timestamp cannot be null")
    public boolean isValid() {
        ResponseData data = getData();
        if (data != null && data.getClaimId() != null) {
            return getMessage() != null && getTimestamp() != null;
        }
        return true;
    }
}
