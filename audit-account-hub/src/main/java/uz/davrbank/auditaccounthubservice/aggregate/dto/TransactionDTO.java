package uz.davrbank.auditaccounthubservice.aggregate.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionDTO implements Serializable {

    String oblast;
    String mfo_client;
    String data;
    String schet;
    String name_schet;
    String nomer_doc;
    String bo;
    String mfo_cor;
    Long obor_debet;
    Long obor_credit;
    String purpose;
    String kassa_simvol;
}
