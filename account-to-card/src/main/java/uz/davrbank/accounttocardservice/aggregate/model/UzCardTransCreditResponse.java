//package uz.davrbank.accounttocardservice.aggregate.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import uz.davrbank.accounttocardservice.aggregate.enums.TranStatus;
//
///**
// */
//
//@Entity
//@Table(name = "account_card_uzcard")
//public class UzCardTransCreditResponse extends BaseEntity {
//
//    @Id
//    private String id;
//
//    @Column(name="account_id")
//    private String accountID;
//
//    @Column(name = "username")
//    private String username;
//    private String refNum;
//    private String ext;
//    private String pan;
//    private String expiry;
//    private String tranType;
//    private String date7;
//    private String date12;
//    private Long amount;
//    private String currency;
//    private String stan;
//    private String field38;
//    private String field48;
//    private String field91;
//    private String merchantId;
//    private String terminalId;
//    private String resp;
//    private String respText;
//    private String respSV;
//    private TranStatus status;
//}