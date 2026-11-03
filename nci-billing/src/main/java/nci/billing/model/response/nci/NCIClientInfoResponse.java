package nci.billing.model.response.nci;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NCIClientInfoResponse {
    private String id;
    private String branch;
    private String number_TAX_REGISTRATION;
    private String post_ADDRESS;
    private String id_CLIENT;
    private String passport_SERIAL;
    private String passport_NUMBER;
    private Object phone_HOME;
    private String passport_DATE_REGISTRATION;
    private String phone_MOBILE;
    private String id_NIBBD;
    private String birthday;
    private String code_RESIDENT;
    private String code_COUNTRY;
    private String code_ADR_REGION;
    private String code_ADR_DISTR;
    private String code_GENDER;
    private String pinfl;
    private String family;
    private String first_NAME;
    private String patronymic;
}
