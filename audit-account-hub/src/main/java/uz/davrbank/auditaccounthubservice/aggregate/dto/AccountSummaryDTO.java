package uz.davrbank.auditaccounthubservice.aggregate.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountSummaryDTO<T> implements Serializable {
    String Period1;
    String Period2;
    String acc_cl;
    String name_cl;
    String ostatka_in;
    String ostatka_out;
    Long total_debet;
    Long total_count;
    Long total_credit;
    List<T> transaction;
}