package davr.yeisvo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.lang.String;
import java.util.List;

/**
 * response DTO for contract history
 * api/contract/history
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContractGetHistoryRes extends BaseResponse {

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
        private int industrialZoneCode;
        private String contractorUzAddress;
        private String contractorUzPhone;
        private int cntrSubject;
        private String currCode1;
        private String currCode2;
        private double amount1;
        private String amount2;
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
        private String creditRate;
        private String leasingFlag;
        private String productsName;
        private String docNo;
        private String docDate;
        private String execType;
        private String execDate;
        private String reestrRegNo;
        private String softwareName;
        private int cntrStatus;
        private String lockDate;
        private int status;
        private String cstDate;
        private String contractorUzInn;
        private double receivableTotal1;
        private double receivableOverdue1;
        private double receivableTotal2;
        private double receivableOverdue2;
        private double payableTotal1;
        private double payableOverdue1;
        private double payableTotal2;
        private double payableOverdue2;
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
        private List<DeclarationsResponse> declarationsResponses;
        private List<GuaranteeResponse> guarantees;
        private List<LetterOfCreditResponse> lettersOfCredit;
        private List<PolicyResponse> policies;
        private List<ReceivableDropResponse> receivableDrops;
        private List<PayableDropResponse> payableDrops;
        private List<PenaltyResponse> penalties;
        private List<CommissionResponse> commissions;
        private List<TaxAgencyPenaltyResponse> taxAgencyPenalties;
        private List<ReceivableInfoResponse> receivableInfos;
        private List<FundResponse> funds;
        private List<RefundExpResponse> refundsExp;
        private List<MoveToExResponse> movesToEx;
        private List<MoveFromExResponse> movesFromEx;
        private List<PaymentRefResponse> paymentRefs;
        private List<TaxResponse> taxes;
        private List<CompensationResponse> compensations;
        private List<SaleResponse> sales;
        private List<LeasePaymentResponse> leasePayments;
        private List<PaymentResponse> payments;
        private List<RefundImpResponse> refundsImp;
        private List<MoveToImResponse> movesToIm;
        private List<MoveFromImResponse> movesFromIm;
        private List<ExchangePaymentsResponse> exchangePayments;

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
            private List<FareResponse> fare;
            private List<ServiceResponse> services;


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
            private List<ConfirmationResponse> confirmations;
            private List<GoodsResponse> goods;
            private List<ServiceResponse> services;
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
            private String amount2;
            private int cntrSubject;
            private String productsName;
            private int execType;
            private String execDate;
            private String creditRate;
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
            private String paymentTerm;
            private String paymentDeadline;
            private int importTerm;
            private String importDeadline;
            private String importTermAfterAccr;
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
            private List<ConfirmationResponse> confirmations;
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
        public static class DeclarationsResponse {
            private String g7a;
            private String g7c;
            private String g7b;
            private String g17;
            private String g24a;
            private String gdvipdate;
            private String gc3date;
            private String g2name;
            private double g23;
            private String g8name;
            private String g9name;
            private String g1;
            private String g22a;
            private double g22b;
            private String g15;
            private List<GoodsDecResponse> goodsDec;

            @Data
            public static class GoodsDecResponse {
                private int g32;
                private String g33;
                private double g42;
                private String g37c;
                private String g31name;
                private int g31amount;
                private String g41;
                private double g38;
                private String g37b;
            }
        }

        @Data
        public static class GuaranteeResponse {
            private String actionType;
            private String bankForName;
            private String docNo;
            private String docStartDate;
            private String currCode;
            private double amount;
            private double currRate;
            private double amount1;
            private double amount2;
            private String docValDate;
            private int docValDays;
            private String userName;
            private int rateType;
            private String insTime;
            private int status;
            private List<GuaranteeTimeChangesResponse> guaranteeTimeChanges;
            private List<GuaranteeSumChangesResponse> guaranteeSumChanges;
            private List<ConfirmationResponse> confirmations;


            @Data
            public static class GuaranteeTimeChangesResponse {
                private int actionType;
                private String newDocValDate;
                private int newDocValDays;
                private int reasonType;
                private String agrmNo;
                private String otherReason;
                private int serialNo;
                private String userName;
                private String docValDate;
                private int docValDays;
                private String insTime;
                private int status;
                private List<ConfirmationResponse> confirmations;


            }

            @Data
            public static class GuaranteeSumChangesResponse {
                private int actionType;
                private double newAmount;
                private double currRate;
                private double newAmount1;
                private double newAmount2;
                private int reasonType;
                private String agrmNo;
                private String otherReason;
                private int serialNo;
                private String userName;
                private int rateType;
                private double amount;
                private String insTime;
                private int status;
                private List<ConfirmationResponse> confirmations;


            }
        }

        @Data
        public static class LetterOfCreditResponse {
            private int actionType;
            private String docNo;
            private String docStartDate;
            private String currCode;
            private double amount;
            private double currRate;
            private double amount1;
            private double amount2;
            private int docValDays;
            private String userName;
            private int fundsAvlblFlag;
            private int fundsBoughtFlag;
            private int fundsWillBeBoughtFlag;
            private int rateType;
            private String insTime;
            private int status;
            private List<LetterOfCreditSumChangesResponse> letterOfCreditSumChanges;
            private List<LetterOfCreditTimeChangesResponse> letterOfCreditTimeChanges;
            private List<ConfirmationResponse> confirmation;


            @Data
            public static class LetterOfCreditSumChangesResponse {
                private int actionType;
                private double newAmount;
                private double currRate;
                private double newAmount1;
                private double newAmount2;
                private int reasonType;
                private String agrmNo;
                private String otherReason;
                private int serialNo;
                private String userName;
                private int rateType;
                private double amount;
                private String insTime;
                private int status;
                private List<ConfirmationResponse> confirmation;
            }

            @Data
            public static class LetterOfCreditTimeChangesResponse {
                private int actionType;
                private int newDocValDays;
                private int reasonType;
                private String agrmNo;
                private String otherReason;
                private int serialNo;
                private String userName;
                private int docValDays;
                private String insTime;
                private int status;
                private List<ConfirmationResponse> confirmation;

            }

        }

        @Data
        public static class PolicyResponse {
            private int actionType;
            private String insuranceCompanyInn;
            private String docNo;
            private String docDate;
            private String currCode;
            private double amount;
            private double currRate;
            private double amount1;
            private double amount2;
            private String docValDate;
            private int docValDays;
            private String userName;
            private int rateType;
            private String insuranceCompanyName;
            private String insTime;
            private int status;
            private List<PolicySumChanges> policySumChanges;
            private List<PolicyTimeChanges> policyTimeChanges;
            private List<ConfirmationResponse> confirmation;

            @Data
            public static class PolicySumChanges {
                private int actionType;
                private double newAmount;
                private double currRate;
                private double newAmount1;
                private double newAmount2;
                private int reasonType;
                private String agrmNo;
                private String otherReason;
                private int serialNo;
                private String userName;
                private String docDate;
                private int rateType;
                private double amount;
                private String insTime;
                private int status;
                private List<ConfirmationResponse> confirmation;
            }

            @Data
            public static class PolicyTimeChanges {
                private int actionType;
                private String newDocValDate;
                private int newDocValDays;
                private int reasonType;
                private String agrmNo;
                private String otherReason;
                private int serialNo;
                private String userName;
                private String docDate;
                private String docValDate;
                private int docValDays;
                private String insTime;
                private int status;
                private List<ConfirmationResponse> confirmation;


            }
        }

        @Data
        public static class ReceivableDropResponse {
            private int actionType;
            private String currCode;
            private double amount;
            private int reasonType;
            private String courtName;
            private String courtDscnNo;
            private String courtDscnDate;
            private String dropAgency;
            private String dropAgencyDscnNo;
            private String dropAgencyDscnDate;
            private String govDscnNo;
            private String govDscnDate;
            private int serialNo;
            private double amount1;
            private double amount2;
            private String docDate;
            private String userName;
            private String remark;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;


        }

        @Data
        public static class PayableDropResponse {
            private int actionType;
            private String currCode;
            private double amount;
            private int reasonType;
            private String agrmNo;
            private String courtName;
            private String courtDscnNo;
            private String courtDscnDate;
            private String dropAgency;
            private String dropAgencyDscnNo;
            private String dropAgencyDscnDate;
            private String otherReason;
            private int serialNo;
            private double amount1;
            private double amount2;
            private String docDate;
            private String userName;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class PenaltyResponse {
            private int actionType;
            private int penaltyType;
            private String docDate;
            private String currCode;
            private double amount;
            private String remark;
            private int serialNo;
            private String userName;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class CommissionResponse {
            private int actionType;
            private int paymentSerialNo;
            private double amount;
            private double amount1;
            private double amount2;
            private int serialNo;
            private String userName;
            private int fundSerialNo;
            private double currRate;
            private int rateType;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class TaxAgencyPenaltyResponse {
            private int actionType;
            private String docDate;
            private double amountSoum;
            private String currCode;
            private double receivableAmount;
            private String courtName;
            private String courtDscnNo;
            private String courtDscnDate;
            private int serialNo;
            private String userName;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class ReceivableInfoResponse {
            private int actionType;
            private String tempImpCurrCode;
            private double tempImpAmount;
            private int customsMode;
            private String remark;
            private int serialNo;
            private String userName;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class FundResponse {
            private int actionType;
            private int serialNo;
            private String docNo;
            private String docDate;
            private int fundDestType;
            private String centralBankDscnNo;
            private String centralBankDscnDate;
            private String bankForName;
            private int conditionType;
            private String letterOfCreditNo;
            private String guaranteeNo;
            private String policyNo;
            private String currCode;
            private double amount;
            private double currRate;
            private double goodsAmount1;
            private double goodsAmount2;
            private double serviceAmount1;
            private double serviceAmount2;
            private String userName;
            private int rateType;
            private String bankAccountant;
            private int cashFlag;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;


        }

        @Data
        public static class RefundExpResponse {
            private int actionType;
            private String docDate;
            private int fundSerialNo;
            private double amount;
            private double currRate;
            private double goodsAmount1;
            private double goodsAmount2;
            private double serviceAmount1;
            private double serviceAmount2;
            private int serialNo;
            private int moveFromExSerialNo;
            private String userName;
            private int rateType;
            private String bankAccountant;
            private int fundDestType;
            private String docNo;
            private String countryCode;
            private String offshoreCode;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class MoveToExResponse {
            private int actionType;
            private int fundSerialNo;
            private double sourceAmount;
            private int moveFromExSerialNo;
            private double currRate;
            private double sourceGoodsAmount1;
            private double sourceGoodsAmount2;
            private double sourceServiceAmount1;
            private double sourceServiceAmount2;
            private int saleType;
            private String destinationIdn;
            private int fundDestType;
            private int conditionType;
            private String letterOfCreditNo;
            private String guaranteeNo;
            private String policyNo;
            private String currCode;
            private double currRate12;
            private double destinationAmount;
            private double accCurrRate;
            private double destinationGoodsAmount1;
            private double destinationGoodsAmount2;
            private double destinationServiceAmount1;
            private double destinationServiceAmount2;
            private int reasonType;
            private String sourceAgrmNo;
            private String destinationAgrmNo;
            private String courtName;
            private String courtDscnNo;
            private String courtDscnDate;
            private String otherReason;
            private String docDate;
            private int serialNo;
            private String userName;
            private int rateType;
            private int rateType12;
            private int accRateType;
            private int destinationMoveFromExSerialNo;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;
        }

        @Data
        public static class MoveFromExResponse {
            private String docDate;
            private String sourceIdn;
            private int fundDestType;
            private int conditionType;
            private String letterOfCreditNo;
            private String guaranteeNo;
            private String policyNo;
            private String currCode;
            private double amount;
            private double currRate;
            private double goodsAmount1;
            private double goodsAmount2;
            private double serviceAmount1;
            private double serviceAmount2;
            private int reasonType;
            private String destinationAgrmNo;
            private String courtName;
            private String courtDscnNo;
            private String courtDscnDate;
            private String otherReason;
            private int serialNo;
            private String userName;
            private String sourceFundDate;
            private int rateType;
            private int saleType;
            private int sourceMoveToExSerialNo;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class PaymentRefResponse {
            private int actionType;
            private String docNo;
            private String docDate;
            private int reasonType;
            private int fundSerialNo;
            private String letterOfCreditNo;
            private String guaranteeNo;
            private String policyNo;
            private double amount;
            private String currCode;
            private String docValDate;
            private String bankAccountant;
            private String bankManager;
            private String mainBranchUserName;
            private String userName;
            private int moveFromExSerialNo;
            private String tnCode;
            private double unusedAmount;
            private String insTime;
            private int status;
            private List<PaymentRefSumChangesResponse> paymentRefSumChanges;
            private List<ConfirmationResponse> confirmation;


            @Data
            public static class PaymentRefSumChangesResponse {
                private int actionType;
                private double newAmount;
                private String reason;
                private String bankAccountant;
                private String bankManager;
                private int serialNo;
                private String userName;
                private String mainBranchUserName;
                private String insTime;
                private int status;
                private List<ConfirmationResponse> confirmation;

            }
        }

        @Data
        public static class TaxResponse {
            private int actionType;
            private String docDate;
            private String countryCode;
            private String currCode;
            private double amount;
            private double currRate1;
            private double currRate2;
            private double amount1;
            private double amount2;
            private String docName;
            private int serialNo;
            private String userName;
            private int rateType1;
            private int rateType2;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class CompensationResponse {
            private int actionType;
            private String policyNo;
            private String currCode;
            private double amount;
            private String docDate;
            private double currRate1;
            private double currRate2;
            private double amount1;
            private double amount2;
            private int serialNo;
            private String userName;
            private int rateType1;
            private int rateType2;
            private String insuranceCompany;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class SaleResponse {
            private int actionType;
            private int saleType;
            private String requirementNo;
            private String requirementDate;
            private String requirementCurrCode;
            private double requirementAmount;
            private String taxAgencyCode;
            private int saleSourceCode;
            private int fundSerialNo;
            private int benefitsInfoCode;
            private int benefitsType;
            private String partialExemptionReason;
            private int saleReasonType;
            private String normativeDocName;
            private String normativeDocNo;
            private String normativeDocDate;
            private String otherReason;
            private double freeAmount;
            private double commissionAmount;
            private double amount;
            private String docDate;
            private int serialNo;
            private String userName;
            private int moveFromExSerialNo;
            private int compensationSerialNo;
            private String remark;
            private String insTime;
            private int status;
            private List<SalesReasonsResponse> salesReasons;
            private List<ConfirmationResponse> confirmation;


            @Data
            public static class SalesReasonsResponse {
                private int code;
                private double sum;

            }
        }

        @Data
        public static class LeasePaymentResponse {
            private int actionType;
            private String docDate;
            private String currCode;
            private double amount;
            private int serialNo;
            private String userName;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class PaymentResponse {
            private String actionType;
            private String docDate;
            private int paymentSourceType;
            private String centralBankDscnNo;
            private String centralBankDscnDate;
            private String bankForName;
            private int fundSourceType;
            private String fundType;
            private String loanType;
            private String otherLoan;
            private int conditionType;
            private String letterOfCreditNo;
            private String guaranteeNo;
            private String currCode;
            private double amount;
            private String currRate;
            private double goodsAmount1;
            private String goodsAmount2;
            private String serviceAmount1;
            private String serviceAmount2;
            private int serialNo;
            private String docNo;
            private String userName;
            private String rateType;
            private String bankAccountant;
            private String countryCode;
            private String offshoreCode;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;
        }

        @Data
        public static class RefundImpResponse {
            private int actionType;
            private String docDate;
            private int paymentSourceType;
            private int paymentSerialNo;
            private String currCode;
            private double amount;
            private double currRate;
            private double goodsAmount1;
            private double goodsAmount2;
            private double serviceAmount1;
            private double serviceAmount2;
            private int serialNo;
            private int moveFromImSerialNo;
            private double accCurrRate;
            private String userName;
            private int rateType;
            private String bankAccountant;
            private int accRateType;
            private double accAmount;
            private String docNo;
            private int cashFlag;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmations;

        }

        @Data
        public static class MoveToImResponse {
            private int actionType;
            private int paymentSerialNo;
            private double sourceAmount;
            private double currRate;
            private double sourceGoodsAmount1;
            private double sourceGoodsAmount2;
            private double sourceServiceAmount1;
            private double sourceServiceAmount2;
            private String destinationIdn;
            private int fundSourceType;
            private int fundType;
            private int loanType;
            private int conditionType;
            private String letterOfCreditNo;
            private String guaranteeNo;
            private String destinationCurrCode;
            private double currRate12;
            private double destinationAmount;
            private double accCurrRate;
            private double destinationGoodsAmount1;
            private double destinationGoodsAmount2;
            private double destinationServiceAmount1;
            private double destinationServiceAmount2;
            private int reasonType;
            private String sourceAgrmNo;
            private String destinationAgrmNo;
            private String courtName;
            private String courtDscnNo;
            private String courtDscnDate;
            private String otherReason;
            private int serialNo;
            private String docDate;
            private int paymentSourceType;
            private String bankForName;
            private String otherLoan;
            private String centralBankDscnNo;
            private String centralBankDscnDate;
            private String userName;
            private int rateType;
            private int rateType12;
            private int accRateType;
            private int destinationMoveFromImSerialNo;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;
        }

        @Data
        public static class MoveFromImResponse {
            private String docDate;
            private String sourceIdn;
            private int paymentSourceType;
            private String centralBankDscnNo;
            private String centralBankDscnDate;
            private String bankForName;
            private int fundSourceType;
            private int fundType;
            private int loanType;
            private String otherLoan;
            private int conditionType;
            private String letterOfCreditNo;
            private String guaranteeNo;
            private String currCode;
            private double currRate;
            private double amount;
            private double goodsAmount1;
            private double goodsAmount2;
            private double serviceAmount1;
            private double serviceAmount2;
            private int reasonType;
            private String agrmNo;
            private String courtName;
            private String courtDscnNo;
            private String courtDscnDate;
            private String otherReason;
            private int serialNo;
            private String userName;
            private String paymentDate;
            private int rateType;
            private int sourceMoveToImSerialNo;
            private String insTime;
            private int status;
            private List<ConfirmationResponse> confirmation;

        }

        @Data
        public static class ExchangePaymentsResponse {
            private String docDate;
            private double amountSoum;
            private double currRate1;
            private double currRate2;
            private double goodsAmount1;
            private double goodsAmount2;
            private String docNo;
            private String userName;
            private double serviceAmount1;
            private double serviceAmount2;
            private int serialNo;
            private String insTime;
            private int status;
        }
    }
}
