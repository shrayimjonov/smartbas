package uz.davrbank.yeisvoservice.aggregate.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * DTO for api/contract/list
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContractGetListRes extends BaseResponse {

    private Long gtkId;

    private List<Contract> contracts;

    @Data
    public static class Contract{

        private String idn;

        private String bankInn;

        private String bankId;

        private int cntrStatus;

        private String contractorUzName;

        private String docNo;

        private String docDate;

        private String contractorForName;

        private String contractorForCountryCode;

        private boolean update;

        private String contractorUzInn;

    }
}
