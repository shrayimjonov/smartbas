package davr.yeisvo.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
public class ContractGetListWithPageResponse extends BaseResponse{

    private Long gtkId;

    private List<ContractGetListRes.Contract> contracts;

    private int size;
    private int page;
    private Long total;

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
