package davr.yeisvo.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContractGetRes extends BaseResponse{  //api/contract/get
    private  Long gtkId;
    private Contract contract;
    @Data
    public static class Contract {
        private Long id;
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
        private String currCode2; // 1
        private Double amount1;
        private Double amount2;
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
        private Integer receivableTotal1;
        private Integer receivableOverdue1;
        private Integer receivableTotal2;
        private Integer receivableOverdue2;
        private Integer payableTotal1;
        private Integer payableOverdue1;
        private Integer payableTotal2;
        private Integer payableOverdue2;
        private String insTime;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;

        private List<Specifications> specifications;
        private List<PerformedServiceDto> performedServices;
        private List<ConsignorDto> consignors;
        private List<ConsigneeDto> consignees;
        private List<ManufacturerDto> manufacturers;
        private List<DeliveryTermDto> deliveryTerms;
        private List<FinanceInfosDto> financeInfos;
        private List<AgreementDto> agreements;

        private EndContractDto endContract;

        private List<ExportTermsDto> exportTerms;
        private List<TollingTermsDto> tollingTerms;
        private List<ImportTermsDto> importTerms;
        private List<BarterTermDto> barterTerms;
        private List<TransferDto> transfers;
        private List<DelegateDto> delegates;
        private List<TradingPlatformDto> tradingPlatforms;
        private List<DeclarationDto> declarations;
        private List<GuaranteeDto> guarantees;
        private List<LetterOfCreditDto> lettersOfCredit;
        private List<PolicyDto> policies;
        private List<ReceivableDropDto> receivableDrops;
        private List<PayableDropDto> payableDrops;
        private List<PenaltyDto> penalties;
        private List<CommissionDto> commissions;
        private List<TaxAgencyPenaltyDto> taxAgencyPenalties;
        private List<ReceivableInfoDto> receivableInfos;
        private List<FundDto> funds;
        private List<RefundExpDto> refundsExp;
        private List<MoveToExDto> movesToEx;
        private List<MoveFromExDto> movesFromEx;
        private List<PaymentRefDto> paymentRefs;
        private List<TaxDto> taxes;
        private List<CompensationDto> compensations;
        private List<SaleDto> sales;
        private List<LeasePaymentDto> leasePayments;
        private List<PaymentDto> payments;
        private List<RefundImpDto> refundsImp;
        private List<MoveToImDto> movesToIm;
        private List<MoveFromImDto> movesFromIm;
        private List<ExchangePaymentDto> exchangePayments;

        @Data
        public static class Specifications {
            private String docDate;
            private Double amount;
            private String agrmNo;
            private String currCode;
            private String cstDate;
            private Integer docType;
            private String docNo;
            private Integer imExFlag;
            private Integer serialNo;
            private String insTime;
            private String oldNo;
            private Integer status;

            private List<Confirmations> confirmations;
            private List<GoodsDto> goods;
            private FareDto fare;
            private List<ServiceDto> services;


            @Data
            public static class Confirmations {
                private String userName;
                private Integer confirm;
                private Integer orgType;
                private String reason;
                private String date;
                private Integer actionType;
            }

            @Data
            public static class GoodsDto {
                private String tnCode;
                private String itemsName;
                private Integer serialNo;
                private String unitCode;
                private Double quantity;
                private String currCode;
                private Double cost;
                private Double amount;
                private String insTime;
                private Integer status;
            }

            @Data
            public static class FareDto {
                private String currCode;
                private Double amount;
                private String insTime;
                private Integer status;
            }

            @Data
            public static class ServiceDto {
                private Integer serialNo;
                private String executorName;
                private String executorCountryCode;
                private String executionCountryCode;
                private String serviceName;
                private String serviceType;
                private String currCode;
                private Double amount;
                private Integer selfMadeFlag;
                private String insTime;
                private Integer status;
            }


        }

        @Data
        public static class PerformedServiceDto {
            private String docDate;
            private String docNo;
            private Integer docType;
            private String currCode1;
            private Double amount1;
            private Double amount2;
            private String currCode2;
            private Integer imExFlag;
            private String insTime;
            private Integer status;
            private List<Specifications.Confirmations> confirmations;
            private List<Specifications.ServiceDto> services;
            private List<Specifications.GoodsDto> goods;
        }

        @Data
        public static class ConsignorDto {
            private Integer serialNo;
            private String name;
            private String countryCode;
            private String inn;
            private String insTime;
            private Integer status;

            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class ConsigneeDto {
            private Integer serialNo;
            private String name;
            private String countryCode;
            private String inn;
            private String insTime;
            private Integer status;
            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class ManufacturerDto {
            private Integer serialNo;
            private String name;
            private String countryCode;
            private String inn;
            private String insTime;
            private Integer status;
            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class DeliveryTermDto {
            private String destination;
            private String incotermsCode;
            private String agrmNo;
            private Integer serialNo;
            private String insTime;
            private Integer status;
            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class FinanceInfosDto {
            private String companyForName;
            private String companyForCountryCode;
            private String companyForAddress;
            private String bankForName;
            private String bankForCountryCode;
            private String bankForAddress;
            private String bankForAttributes;
            private String agrmNo;
            private Integer serialNo;
            //        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            private String insTime;
            private Integer status;

            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class AgreementDto {
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
            private Double amount1;
            private Double amount2;
            private Integer cntrSubject;
            private String productsName;
            private Integer execType;
            private String execDate;
            private Integer creditRate;
            private String cstDate;
            private Integer serialNo;
            private String insTime;
            private Integer industrialZoneCode;
            private Integer status;
            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class EndContractDto {
            private String docDate;
            private String remark;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class ExportTermsDto {
            private Integer exportType;
            private String exportDeadline;
            private Integer exportTerm;
            private Integer paymentTerm;
            private String paymentDeadline;
            private Integer leasingDeadline;
            private Integer leasingRate;
            private Integer deadlineType;
            private String agrmNo;
            private String insTime;
            private Integer status;
            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class TollingTermsDto {
            private String idCol;
            private String goodsTitle;
            private String currCode;
            private Double currRate1;
            private Double currRate2;
            private Integer serviceAmount1;
            private Integer serviceAmount2;
            private String docDate;
            private Integer rateType1;
            private Integer rateType2;
            private Double goodsAmount;
            private Integer serialNo;
            private String insTime;
            private Integer status;
            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class ImportTermsDto {
            private String paymentMethod;
            private Integer paymentTerm;
            private String paymentDeadline;
            private Integer importTerm;
            private String importDeadline;
            private Integer importTermAfterAccr;
            private String agrmNo;
            private String insTime;
            private Integer status;
            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class BarterTermDto {
            private String paymentMethod;
            private String exportDeadline;
            private Integer exportTerm;
            private Integer importTerm;
            private Integer paymentTerm;
            private String agrmNo;
            private String insTime;
            private Integer status;
            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class TransferDto {
            private Integer transferType;
            private String agrmNo;
            private String newIdn;
            private String dstBankId;
            private String dstBankInn;
            private String dstExchInn;
            private String remark;
            private Integer serialNo;
            private String srcBankUserName;
            private String dstBankUserName;
            private Integer srcBankConfirmationFlag;
            private Integer dstBankConfirmationFlag;
            private String dstExchName;
            private String insTime;
            private String oldIdn;
            private Integer status;
        }

        @Data
        public static class DelegateDto {
            private Integer reasonType;
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
            private Integer dstIndustrialZoneCode;
            private String dstAddress;
            private String dstPhone;
            private String remark;
            private Integer serialNo;
            private Integer srcBankConfirmationFlag;
            private Integer dstBankConfirmationFlag;
            private String srcBankUserName;
            private String dstBankUserName;
            private String insTime;
            private String oldIdn;
            private String parentCompanyCode;
            private String newIdn;
            private Integer status;
            private String dstBankInn;
        }

        @Data
        public static class TradingPlatformDto {
            private Integer serialNo;
            private Integer docNo;
            private String name;
            private String url;
            private String countryCode;
            private Integer ownFlag;
            private String insTime;
            private Integer status;
            private List<Specifications.Confirmations> confirmations;
        }

        @Data
        public static class DeclarationDto {
            private String g7a;
            private String g7c;
            private String g7b;
            private String g17;
            private String g24a;
            private String gdvipdate;
            private String gc3date;
            private String g2name;
            private Double g23;
            private String g8name;
            private String g9name;
            private String g1;
            private String g22a;
            private Integer g22b;
            private String g15;
            private List<DeclarationGoodsDto> goods;

            @Data
            public static class DeclarationGoodsDto {
                private Integer g32;
                private String g33;
                private Integer g42;
                private String g37c;
                private String g31name;
                private Integer g31amount;
                private String g41;
                private Integer g38;
                private String g37b;
            }
        }

        @Data
        public static class GuaranteeDto {
            private String docNo;
            private String docStartDate;
            private String currCode;
            private Double amount;
            private String docValDate;
            private Integer docValDays;
            private String userName;
            private String insTime;
            private Integer status;
            private List<GuaranteeTimeChangeDto> guaranteeTimeChanges;
            private List<GuaranteeSumChangeDto> guaranteeSumChanges;

            @Data
            public static class GuaranteeTimeChangeDto {
                private String docValDate;
                private Integer docValDays;
                private String newDocValDate;
                private Integer newDocValDays;
                private Integer reasonType;
                private Integer serialNo;
                private String userName;
                private String insTime;
                private Integer status;
            }

            @Data
            public static class GuaranteeSumChangeDto {
                private Double amount;
                private Double newAmount;
                private Integer reasonType;
                private Integer serialNo;
                private String userName;
                private String insTime;
                private Integer status;
            }
        }

        @Data
        public static class LetterOfCreditDto {
            private String docNo;
            private String docStartDate;
            private String currCode;
            private Double amount;
            private Integer docValDays;
            private String userName;
            private String insTime;
            private Integer status;
            private List<LetterOfCreditSumChangeDto> letterOfCreditSumChanges;
            private List<LetterOfCreditTimeChangeDto> letterOfCreditTimeChanges;

            @Data
            public static class LetterOfCreditSumChangeDto {
                private Double amount;
                private Double newAmount;
                private Integer reasonType;
                private Integer serialNo;
                private String userName;
                private String insTime;
                private Integer status;
            }

            @Data
            public static class LetterOfCreditTimeChangeDto {
                private Integer docValDays;
                private Integer newDocValDays;
                private Integer reasonType;
                private Integer serialNo;
                private String userName;
                private String insTime;
                private Integer status;
            }
        }

        @Data
        public static class PolicyDto {
            private String docNo;
            private String docDate;
            private String currCode;
            private Double amount;
            private String docValDate;
            private Integer docValDays;
            private String userName;
            private String insTime;
            private Integer status;
            private List<PolicySumChangeDto> policySumChanges;
            private List<PolicyTimeChangeDto> policyTimeChanges;

            @Data
            public static class PolicySumChangeDto {
                private Double amount;
                private Double newAmount;
                private Integer reasonType;
                private Integer serialNo;
                private String userName;
                private String insTime;
                private Integer status;
            }

            @Data
            public static class PolicyTimeChangeDto {
                private String docValDate;
                private Integer docValDays;
                private String newDocValDate;
                private Integer newDocValDays;
                private Integer reasonType;
                private Integer serialNo;
                private String userName;
                private String insTime;
                private Integer status;
            }
        }

        @Data
        public static class ReceivableDropDto {
            private String currCode;
            private Double amount;
            private Integer reasonType;
            private Integer serialNo;
            private String docDate;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class PayableDropDto {
            private String currCode;
            private Double amount;
            private Integer reasonType;
            private Integer serialNo;
            private String docDate;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class PenaltyDto {
            private Integer penaltyType;
            private String docDate;
            private String currCode;
            private Double amount;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class CommissionDto {
            private Integer paymentSerialNo;
            private Double amount;
            private Integer serialNo;
            private String userName;
            private Integer fundSerialNo;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class TaxAgencyPenaltyDto {
            private String docDate;
            private Double amountSoum;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class ReceivableInfoDto {
            private String tempImpCurrCode;
            private Double tempImpAmount;
            private Integer customsMode;
            private String remark;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class FundDto {
            private Integer serialNo;
            private String docNo;
            private String docDate;
            private Integer conditionType;
            private String currCode;
            private Double amount;
            private Double currRate;
            private Double goodsAmount1;
            private Double goodsAmount2;
            private Double serviceAmount1;
            private Double serviceAmount2;
            private String userName;
            private Integer rateType;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class RefundExpDto {
            private String docDate;
            private Integer fundSerialNo;
            private Double amount;
            private Integer serialNo;
            private Integer moveFromExSerialNo;
            private String userName;
            private String docNo;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class MoveToExDto {
            private Integer fundSerialNo;
            private Double sourceAmount;
            private String destinationIdn;
            private String docDate;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class MoveFromExDto {
            private String docDate;
            private String sourceIdn;
            private String currCode;
            private Double amount;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
            private Double currRate;
            private Double goodsAmount1;
            private Double goodsAmount2;
            private Double serviceAmount1;
            private Double serviceAmount2;
            private Integer rateType;
        }

        @Data
        public static class PaymentRefDto {
            private String docNo;
            private String docDate;
            private Integer reasonType;
            private Integer fundSerialNo;
            private String letterOfCreditNo;
            private String guaranteeNo;
            private String policyNo;
            private Double amount;
            private String currCode;
            private Double unusedAmount;
            private String docValDate;
            private String userName;
            private Integer moveFromExSerialNo;
            private String insTime;
            private Integer status;
            private List<PaymentRefSumChangeDto> paymentRefSumChanges;

            @Data
            public static class PaymentRefSumChangeDto {
                private Double newAmount;
                private String reason;
                private Integer serialNo;
                private String userName;
                private String mainBranchUserName;
                private String insTime;
                private Integer status;
            }
        }

        @Data
        public static class TaxDto {
            private String docDate;
            private String countryCode;
            private String currCode;
            private Double amount;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class CompensationDto {
            private String policyNo;
            private String currCode;
            private Double amount;
            private String docDate;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class SaleDto {
            private Integer saleType;
            private Integer fundSerialNo;
            private Integer benefitsInfoCode;
            private Double amount;
            private String docDate;
            private Integer serialNo;
            private String userName;
            private Integer moveFromExSerialNo;
            private Integer compensationSerialNo;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class LeasePaymentDto {
            private String docDate;
            private String currCode;
            private Double amount;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class PaymentDto {
            private String docDate;
            private Integer fundSourceType;
            private String currCode;
            private Double amount;
            private Integer serialNo;
            private String docNo;
            private String userName;
            private String insTime;
            private Integer status;
            private Double currRate;
            private Double goodsAmount1;
            private Double goodsAmount2;
            private Double serviceAmount1;
            private Double serviceAmount2;
            private Integer rateType;
        }

        @Data
        public static class RefundImpDto {
            private String docDate;
            private String currCode;
            private Double amount;
            private Integer serialNo;
            private String userName;
            private String docNo;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class MoveToImDto {
            private Integer paymentSerialNo;
            private Double sourceAmount;
            private String destinationIdn;
            private Integer serialNo;
            private String docDate;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        public static class MoveFromImDto {
            private String docDate;
            private String sourceIdn;
            private String currCode;
            private Double amount;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
            private Double currRate;
            private Double goodsAmount1;
            private Double goodsAmount2;
            private Double serviceAmount1;
            private Double serviceAmount2;
            private Integer rateType;
        }

        @Data
        public static class ExchangePaymentDto {
            private String docDate;
            private Double amountSoum;
            private String docNo;
            private String userName;
            private Integer serialNo;
            private String insTime;
            private Integer status;
        }
    }
}
