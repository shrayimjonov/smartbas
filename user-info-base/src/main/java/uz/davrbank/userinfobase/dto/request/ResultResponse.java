package uz.davrbank.userinfobase.dto.request;

import lombok.*;
import uz.davrbank.userinfobase.entity.nci.PersonDetails;

/**
 * Email : user@example.com
 * * Project name : UserInfoBase
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultResponse {
    private String id;
    private String name;
    private String branch;
    private String sign_VIP;
    private String zip_CODE;
    private String contract_NUM;
    private String date_REGISTRATION;
    private String number_REGISTRATION_DOC;
    private String code_TAX_ORG;
    private String number_TAX_REGISTRATION;
    private String code_SECTOR;
    private String code_ORGAN_DIRECT;
    private String code_CLASS_CREDIT;
    private String code_BANK;
    private String account;
    private String post_ADDRESS;
    private String kod_ERR;
    private String file_NAME;
    private String state;
    private String code_PLACE_REGIST;
    private String code_CITIZENSHIP;
    private String code_CAPACITY;
    private String capacity_STATUS_DATE;
    private String capacity_STATUS_PLACE;
    private String num_CERTIF_CAPACITY;
    private String email_ADDRESS;
    private String pension_SERTIF_SERIAL;
    private String code_NATION;
    private String code_BIRTH_REGION;
    private String code_BIRTH_DISTR;
    private String type_DOCUMENT;
    private String passport_DATE_EXPIRATION;
    private String inps;
    private String date_OPEN_NIBBD;
    private String date_CLOSE_NIBBD;
    private String close_TYPE;
    private String close_DOC_N;
    private String close_DOC_D;
    private String drive_PERMIT_SER;
    private String drive_PERMIT_NUM;
    private String drive_PERMIT_REG_D;
    private String drive_PERMIT_EXP_D;
    private String drive_PERMIT_PLACE;
    private String agreement;
    private String code_SUBJECT;
    private String client_ID;
    private String passport_TYPE;
    private String passport_PLACE_REGISTRATION;
    private String birth_PLACE;
    private String passport_SERIAL;
    private String passport_NUMBER;
    private String phone_HOME;
    private String passport_DATE_REGISTRATION;
    private String phone_MOBILE;
    private String id_NIBBD;
    private String birthday;
    private String code_RESIDENT;
    private String code_ADR_REGION;
    private String code_ADR_DISTR;
    private String code_GENDER;
    private String pinfl;
    private String family;
    private String first_NAME;
    private String patronymic;

    public static ResultResponse fromEntity(PersonDetails entity) {
        return ResultResponse.builder()
                .id(entity.getId() != null ? entity.getId().toString() : null)
                .name(entity.getName())
                .branch(entity.getBranch())
                .contract_NUM(entity.getContractNum())
                .date_REGISTRATION(entity.getDateRegistration())
                .number_REGISTRATION_DOC(entity.getNumberRegistrationDoc())
                .code_BANK(entity.getCodeBank())
                .account(entity.getAccount())
                .post_ADDRESS(entity.getPostAddress())
                .kod_ERR(entity.getKodErr())
                .file_NAME(entity.getFileName())
                .state(entity.getState())
                .code_CITIZENSHIP(entity.getCodeCitizenship())
                .code_CAPACITY(entity.getCodeCapacity())
                .type_DOCUMENT(entity.getTypeDocument())
                .passport_DATE_EXPIRATION(entity.getPassportDateExpiration())
                .inps(entity.getInps())
                .date_OPEN_NIBBD(entity.getDateOpenNibbd())
                .date_CLOSE_NIBBD(entity.getDateCloseNibbd())
                .code_SUBJECT(entity.getCodeSubject())
                .client_ID(entity.getClientId())
                .passport_TYPE(entity.getPassportType())
                .passport_PLACE_REGISTRATION(entity.getPassportPlaceRegistration())
                .birth_PLACE(entity.getBirthPlace())
                .passport_SERIAL(entity.getPassportSerial())
                .passport_NUMBER(entity.getPassportNumber())
                .phone_HOME(entity.getPhoneHome())
                .passport_DATE_REGISTRATION(entity.getPassportDateRegistration())
                .phone_MOBILE(entity.getPhoneMobile())
                .id_NIBBD(entity.getIdNibbd())
                .birthday(entity.getBirthday())
                .code_RESIDENT(entity.getCodeResident())
                .code_ADR_REGION(entity.getCodeAdrRegion())
                .code_ADR_DISTR(entity.getCodeAdrDistr())
                .code_GENDER(entity.getCodeGender())
                .pinfl(entity.getPinfl())
                .family(entity.getFamily())
                .first_NAME(entity.getFirstName())
                .patronymic(entity.getPatronymic())
                .build();
    }
}

