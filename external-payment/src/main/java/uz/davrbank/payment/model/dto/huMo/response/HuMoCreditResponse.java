package uz.davrbank.payment.model.dto.huMo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nodir.commondto.model.dto.core.response.CoreError;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HuMoCreditResponse {
    private String pan2;
    private String auth_msg_ref1;
    private String iss_ref_data;
    private Long transaction_amount;
    private String merchant_id;
    private String point_code;
    private String auth_action_code_final;
    private String auth_row_numb1;
    private String paymentRef;
    private Long cardholder_amount;
    private String ccy_code;
    private String auth_appr_code;
    private String auth_stan;
    private String auth_time;
    private String action;
    private String expiry;
    private String centre_id;
    private String conversion_rate;
    private String terminal_id;
    private Long amount;
    private String internal_pan2_masked;
    private String reconcile_info;
    private String merchant_name;
    private String fld_126_MSSD;
    private String card_type;
    private String acq_inst;
    private String cardholder_ccy_code;
    private String auth_action_code;
    private String auth_ref_number;
    private String stip_client_id;
    private String paymentID;
    private CoreError error;
}
