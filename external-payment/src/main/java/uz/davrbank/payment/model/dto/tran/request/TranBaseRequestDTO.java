package uz.davrbank.payment.model.dto.tran.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.payment.model.dto.tran.request.params.TranCreditPersonParam;
import uz.davrbank.payment.model.entity.Transaction;
import uz.davrbank.payment.model.enums.Processing;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranBaseRequestDTO {
    private Long amount;
    private String terminalId;
    private String merchantId;
    private TranCreditPersonParam personParam;
    private String extId;
    private Transaction transaction;
    private TranBaseParams debitParams;
    private TranBaseParams creditParams;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TranBaseParams {
        private String branch;
        private String expiry;
        private String refNum;
        private String account;
        private Processing processing;
    }


}
