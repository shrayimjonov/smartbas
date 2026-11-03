package uz.davrbank.bustreasury.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WiringResultDTO {
    private static final WiringResultDTO instance = new WiringResultDTO();
    private Boolean success;
    private String message;
    private WiringResponseDTO wiringResponse;
    private String error;

    public WiringResultDTO successfully(WiringResponseDTO wiringResponse) {
        this.success = true;
        this.wiringResponse = wiringResponse;
        return this;
    }

    public WiringResultDTO successfullyWithMessage(String message) {
        this.success = true;
        this.message = message;
        return this;
    }
}
