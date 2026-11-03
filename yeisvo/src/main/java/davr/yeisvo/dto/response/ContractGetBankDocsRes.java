package davr.yeisvo.dto.response;

import lombok.*;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContractGetBankDocsRes extends BaseResponse{ //api/contract/get_bank_docs
    private Long gtkId;
    private Contract contract;

    @Data
    private static class Contract {
        private List<Guarantee> guarantees;
        private List<LetterOfCredit> lettersOfCredit;
        private List<Policy> policies;
        private List<ReceivableDrop> receivableDrops;
        private List<PayableDrop> payableDrops;
        private List<Penalty> penalties;
        private List<Commission> commissions;
        private List<TaxAgencyPenalty> taxAgencyPenalties;
        private List<ReceivableInfo> receivableInfos;
        private List<Fund> funds;
        private List<RefundExp> refundsExp;
        private List<MoveToEx> movesToEx;
        private List<MoveFromEx> movesFromEx;
        private List<PaymentRef> paymentRefs;
        private List<Tax> taxes;
        private List<Compensation> compensations;
        private List<Sale> sales;
        private List<LeasePayment> leasePayments;
        private List<Payment> payments;
        private List<RefundImp> refundsImp;
        private List<MoveToIm> movesToIm;
        private List<MoveFromIm> movesFromIm;
        private List<ExchangePayment> exchangePayments;



        @Data
        private static class Guarantee {
            private String docNo;
            private String docStartDate;
            private String currCode;
            private Double amount;
            private String docValDate;
            private Integer docValDays;
            private String userName;
            private String insTime;
            private Integer status;
            private List<GuaranteeTimeChange> guaranteeTimeChanges;
            private List<GuaranteeSumChange> guaranteeSumChanges;

            @Data
            public static class GuaranteeTimeChange {
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
            public static class GuaranteeSumChange {
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
        private static class LetterOfCredit {
            private String docNo;
            private String docStartDate;
            private String currCode;
            private Double amount;
            private Integer docValDays;
            private String userName;
            private String insTime;
            private Integer status;
            private List<LetterOfCreditSumChange> letterOfCreditSumChanges;
            private List<LetterOfCreditTimeChange> letterOfCreditTimeChanges;

            @Data
            private static class LetterOfCreditSumChange {
                private Double amount;
                private Double newAmount;
                private Integer reasonType;
                private Integer serialNo;
                private String userName;
                private String insTime;
                private Integer status;
            }

            @Data
            private static class LetterOfCreditTimeChange {
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
        private static  class Policy {
            private String docNo;
            private String docDate;
            private String currCode;
            private Double amount;
            private String docValDate;
            private Integer docValDays;
            private String userName;
            private String insTime;
            private Integer status;
            private List<PolicySumChange> policySumChanges;
            private List<PolicyTimeChange> policyTimeChanges;

            @Data
            private static class PolicySumChange {
                private Double amount;
                private Double newAmount;
                private Integer reasonType;
                private Integer serialNo;
                private String userName;
                private String insTime;
                private Integer status;
            }

            @Data
            private static class PolicyTimeChange {
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
        private static class ReceivableDrop {
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
        private static class PayableDrop {
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
        private static class Penalty {
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
        private static class Commission {
            private Integer paymentSerialNo;
            private Double amount;
            private Integer serialNo;
            private String userName;
            private Integer fundSerialNo;
            private String insTime;
            private Integer status;
        }
        @Data
        private static class TaxAgencyPenalty {
            private String docDate;
            private Double amountSoum;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        private static class ReceivableInfo {
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
        private static class Fund {
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
        private static class RefundExp {
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
        private static class MoveToEx {
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
        private static class MoveFromEx {
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
        private static class PaymentRef {
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
            private List<PaymentRefSumChange> paymentRefSumChanges;

            @Data
            private static class PaymentRefSumChange {
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
        private static class Tax {
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
        private static class Compensation {
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
        private static class Sale {
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
        private static class LeasePayment {
            private String docDate;
            private String currCode;
            private Double amount;
            private Integer serialNo;
            private String userName;
            private String insTime;
            private Integer status;
        }

        @Data
        private static class Payment {
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
        private static class RefundImp {
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
        private static class MoveToIm {
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
        private static class MoveFromIm {
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
        private static class ExchangePayment {
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
