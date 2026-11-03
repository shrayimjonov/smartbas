package uz.davrbank.payment.model.dto.tran.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.payment.model.dto.backAPI.response.BackSimpleTranResponse;
import uz.davrbank.payment.model.dto.huMo.response.HuMoBasePaymentResponse;
import uz.davrbank.payment.model.dto.huMo.response.HuMoCreditResponse;
import uz.davrbank.payment.model.dto.uzcard.response.result.TransPayPurposeResult;
import uz.davrbank.payment.model.dto.uzcard.response.result.UzCardBasePaymentResult;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranBaseResponseDTO {
    private String refNum;
    private String paymentId;
    private Map<Object, Object> request;
    private Map<Object, Object> response;

    public TranBaseResponseDTO(UzCardBasePaymentResult result, Map<Object, Object> request, Map<Object, Object> response) {
        this.refNum = result.getRefNum();
        this.paymentId = result.getRefNum();
        this.request = request;
        this.response = response;
    }

    public TranBaseResponseDTO(HuMoCreditResponse result, Map<Object, Object> request, Map<Object, Object> response) {
        this.refNum = result.getAuth_ref_number();
        this.paymentId = result.getPaymentID();
        this.request = request;
        this.response = response;
    }

    public TranBaseResponseDTO(BackSimpleTranResponse data, Map<Object, Object> request, Map<Object, Object> response) {
        this.refNum = data.getId();
        this.paymentId = data.getId();
        this.request = request;
        this.response = response;
    }

    public TranBaseResponseDTO(TransPayPurposeResult result, Map<Object, Object> request, Map<Object, Object> response) {
        this.refNum = result.getRefNum();
        this.paymentId = result.getRefNum();
        this.request = request;
        this.response = response;
    }

    public TranBaseResponseDTO(HuMoBasePaymentResponse result, Map<Object, Object> request, Map<Object, Object> response) {
        this.refNum = result.getAuth_ref_number();
        this.paymentId = result.getPaymentID();
        this.request = request;
        this.response = response;
    }
}
