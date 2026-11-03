package nci.billing.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nci.billing.model.dto.ProblemDto;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ClientResponse {
    @JsonProperty("id")
    private String clientId;
    private String branch;
    private String state;
    @JsonProperty("name")
    private String fullName;
    @JsonProperty("date_OPEN_NIBBD")
    private String dateOpenNibbd;
    @JsonProperty("date_CLOSE_NIBBD")
    private String dateCloseNibbd;
    @JsonProperty("number_TAX_REGISTRATION")
    private String inn;
    @JsonProperty("passport_SERIAL")
    private String passportSerial;
    @JsonProperty("passport_NUMBER")
    private String passportNumber;
    private String pinfl;
    @JsonProperty("phone_HOME")
    private String phoneHome;
    @JsonProperty("passport_DATE_REGISTRATION")
    private String passportDateRegistration;
    @JsonProperty("phone_MOBILE")
    private String phoneMobile;
    @JsonProperty("family")
    private String surname;
    @JsonProperty("first_NAME")
    private String name;
    private String patronymic;
    @JsonProperty("id_NIBBD")
    private String idNibbd;
    @JsonProperty("post_ADDRESS")
    private String address;
    private String birthday;
    @JsonProperty("code_ADR_REGION")
    private String codeRegion;
    @JsonProperty("code_ADR_DISTR")
    private String codeDistrict;
    @JsonProperty("code_GENDER")
    private String codeGender;
    @JsonProperty("passport_PLACE_REGISTRATION")
    private String passportPlaceRegistration;
    @JsonProperty("code_BANK")
    private String codeBank;
    @JsonProperty("code_CITIZENSHIP")
    private String codeCitizenship;
    @JsonProperty("birth_PLACE")
    private String birthPlace;
    @JsonProperty("email_ADDRESS")
    private String email;
    @JsonProperty("type_DOCUMENT")
    private String documentType;
    @JsonProperty("passport_DATE_EXPIRATION")
    private String passportDateExpiration;
    @JsonProperty("code_RESIDENT")
    private String resident;
    @JsonProperty("code_SUBJECT")
    private String subject;

    private ProblemDto error;


}
