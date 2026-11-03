package davr.yeisvo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.lang.String;
import java.util.List;


/**
 * response DTO for contract owner docs
 * api/contract/get_owner_docs
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContractGetOwnerDocRes extends BaseResponse {

    private Long gtkId;
    private Contract contract;

    @Data
    private static class Contract {
        private String idn;
        private String rootIdn;
        private String cntrType;
        private String oldIdn;
        private String bankId;
        private String bankInn;
        private String mvesNo;
        private String mvesDate;
        private String govDcsnNo;
        private String govDcsnDate;
        private String okpo;
        private String okonx;
        private String contractorUzParentCompany;
        private String contractorUzName;
        private String rgnCode;
        private Integer industrialZoneCode;
        private String contractorUzAddress;
        private String contractorUzPhone;
        private Integer cntrSubject;
        private String currCode1;
        private String currCode2;
        private BigDecimal amount1;
        private BigDecimal amount2;
        private String accCurrCode1;
        private String accCurrCode2;
        private String contractorForName;
        private String contractorForCountryCode;
        private String contractorForAddress;
        private String bankForName;
        private String bankForCountryCode;
        private String bankForAddress;
        private String bankForAttributes;
        private String centralBankLetterNo;
        private String centralBankLetterDate;
        private Integer creditRate;
        private Integer leasingFlag;
        private String productsName;
        private String docNo;
        private String docDate;
        private Integer execType;
        private String execDate;
        private String reestrRegNo;
        private String softwareName;
        private Integer cntrStatus;
        private String lockDate;
        private int status;
        private String cstDate;
        private String contractorUzInn;
        private BigDecimal receivableTotal1;
        private BigDecimal receivableOverdue1;
        private BigDecimal receivableTotal2;
        private BigDecimal receivableOverdue2;
        private BigDecimal payableTotal1;
        private BigDecimal payableOverdue1;
        private BigDecimal payableTotal2;
        private BigDecimal payableOverdue2;
        private String insTime;
        private List<SpecificationResponse> specifications;
        private List<PerformedServiceResponse> performedServices;
        private List<ConsignorsResponse> consignors;
        private List<ConsigneesResponse> consignees;
        private List<ManufacturersResponse> manufacturers;
        private List<DeliveryTermResponse> deliveryTerms;
        private List<FinanceInfoResponse> financeInfos;
        private List<AgreementResponse> agreements;
        private EndContractResponse endContract;
        private List<ExportTermResponse> exportTerms;
        private List<ImportTermResponse> importTerms;
        private List<BarterTermResponse> barterTerms;
        private List<TollingTermResponse> tollingTerms;
        private List<TransferResponse> transfers;
        private List<DelegateResponse> delegates;
        private List<TradingPlatformResponse> tradingPlatforms;


        @Data
        public static class SpecificationResponse {
            private String docDate;
            private double amount;
            private String agrmNo;
            private String currCode;
            private String cstDate;
            private int docType;
            private String docNo;
            private int imExFlag;
            private int serialNo;
            private String insTime;
            private String oldNo;
            private int status;
            private List<ConfirmationResponse> confirmations;
            private List<GoodsResponse> goods;
            private List<FareResponse> fares;
            private List<ServiceResponse> services;

            @Data
            public static class GoodsResponse {
                private String tnCode;
                private String itemsName;
                private int serialNo;
                private String unitCode;
                private double quantity;
                private String currCode;
                private double cost;
                private double amount;
                private String insTime;
                private int status;
            }

            @Data
            public static class FareResponse {
                private String currCode;
                private double amount;
                private String insTime;
                private int status;
            }

            @Data
            public static class ServiceResponse {
                private int serialNo;
                private String executorName;
                private String executorCountryCode;
                private String executionCountryCode;
                private String serviceName;
                private String serviceType;
                private String currCode;
                private double amount;
                private int selfMadeFlag;
                private String insTime;
                private int status;
            }


        }

        @Data
        public static class PerformedServiceResponse {
            private String docDate;
            private String docNo;
            private int docType;
            private String currCode1;
            private double amount1;
            private double amount2;
            private String currCode2;
            private String insTime;
            private int imExFlag;
            private int status;
            private List<PerformedServiceResponse.ConfirmationResponse> confirmations;
            private List<PerformedServiceResponse.GoodsResponse> goods;
            private List<PerformedServiceResponse.ServiceResponse> services;

            @Data
            public static class ConfirmationResponse {
                private String userName;
                private int confirm;
                private int orgType;
                private String reason;
                private String date;
                private int actionType;
            }

            @Data
            public static class GoodsResponse {
                private String tnCode;
                private String itemsName;
                private int serialNo;
                private String unitCode;
                private double quantity;
                private String currCode;
                private double cost;
                private double amount;
                private String insTime;
                private int status;
            }

            @Data
            public static class ServiceResponse {
                private int serialNo;
                private String executorName;
                private String executorCountryCode;
                private String executionCountryCode;
                private String serviceName;
                private String serviceType;
                private String currCode;
                private double amount;
                private int selfMadeFlag;
                private String insTime;
                private int status;

            }
        }

        @Data
        public static class ConsignorsResponse {
            private int serialNo;
            private String name;
            private String countryCode;
            private String inn;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;

        }

        @Data
        public static class ConsigneesResponse {
            private int serialNo;
            private String name;
            private String countryCode;
            private String inn;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;

        }

        @Data
        public static class ManufacturersResponse {
            private int serialNo;
            private String name;
            private String countryCode;
            private String inn;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;
        }

        @Data
        public static class DeliveryTermResponse {
            private String destination;
            private String incotermsCode;
            private String agrmNo;
            private int serialNo;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;
        }

        @Data
        public static class FinanceInfoResponse {
            private String companyForName;
            private String companyForCountryCode;
            private String companyForAddress;
            private String bankForName;
            private String bankForCountryCode;
            private String bankForAddress;
            private String bankForAttributes;
            private String agrmNo;
            private int serialNo;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;
        }

        @Data
        public static class AgreementResponse {
            private String docNo;
            private String docDate;
            private String mvesDate;
            private String agrmSubject;
            private String currCode1;
            private String currCode2;
            private String accCurrCode1;
            private String accCurrCode2;
            private String contractorForName;
            private String contractorForCountryCode;
            private String contractorForAddress;
            private String bankForName;
            private String bankForCountryCode;
            private String bankForAddress;
            private String bankForAttributes;
            private String contractorUzName;
            private String rgnCode;
            private String contractorUzAddress;
            private double amount1;
            private double amount2;
            private int cntrSubject;
            private String productsName;
            private int execType;
            private String execDate;
            private int creditRate;
            private String cstDate;
            private int serialNo;
            private String insTime;
            private int industrialZoneCode;
            private int status;
            private List<ConfirmationResponse> confirmations;
        }

        @Data
        public static class EndContractResponse {
            private String docDate;
            private String remark;
            private String insTime;
            private int status;
        }

        @Data
        public static class ExportTermResponse {
            private int exportType;
            private String exportDeadline;
            private int exportTerm;
            private int paymentTerm;
            private String paymentDeadline;
            private int leasingDeadline;
            private int leasingRate;
            private int deadlineType;
            private String agrmNo;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;

        }

        @Data
        public static class ImportTermResponse {
            private String paymentMethod;
            private int paymentTerm;
            private String paymentDeadline;
            private int importTerm;
            private String importDeadline;
            private int importTermAfterAccr;
            private String agrmNo;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;


        }

        @Data
        public static class BarterTermResponse {
            private String paymentMethod;
            private String exportDeadline;
            private int exportTerm;
            private int importTerm;
            private int paymentTerm;
            private String agrmNo;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;

        }

        @Data
        public static class TollingTermResponse {
            private String idCol;
            private String goodsTitle;
            private String currCode;
            private double currRate1;
            private double currRate2;
            private double serviceAmount1;
            private double serviceAmount2;
            private String docDate;
            private int rateType1;
            private int rateType2;
            private double goodsAmount;
            private int serialNo;
            private String insTime;
            private int status;
            private List<ConsigneesResponse> confirmations;
        }

        @Data
        public static class TransferResponse {
            private int transferType;
            private String agrmNo;
            private String newIdn;
            private String dstBankId;
            private String dstBankInn;
            private String dstExchInn;
            private String remark;
            private int serialNo;
            private String srcBankUserName;
            private String dstBankUserName;
            private int srcBankConfirmationFlag;
            private int dstBankConfirmationFlag;
            private String dstExchName;
            private String insTime;
            private String oldIdn;
            private int status;
        }

        @Data
        public static class DelegateResponse {
            private int reasonType;
            private String courtName;
            private String courtDscnNo;
            private String courtDscnDate;
            private String govDscnNo;
            private String govDscnDate;
            private String agrmNo;
            private String dstInn;
            private String dstBankId;
            private String dstOkpo;
            private String dstOkonx;
            private String dstName;
            private String dstRgnCode;
            private int dstIndustrialZoneCode;
            private String dstAddress;
            private String dstPhone;
            private String remark;
            private int serialNo;
            private int srcBankConfirmationFlag;
            private int dstBankConfirmationFlag;
            private String srcBankUserName;
            private String dstBankUserName;
            private String insTime;
            private String oldIdn;
            private String parentCompanyCode;
            private String newIdn;
            private int status;
            private String dstBankInn;
        }

        @Data
        public static class TradingPlatformResponse {
            private int serialNo;
            private int docNo;
            private String name;
            private String url;
            private String countryCode;
            private int ownFlag;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;


        }

        @Data
        public static class ConfirmationResponse {
            private String userName;
            private int confirm;
            private int orgType;
            private String reason;
            private String date;
            private int actionType;
        }
    }
}
