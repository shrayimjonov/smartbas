package uz.davrbank.accounttocardservice.aggregate.enums;

public enum TranStatus {
    OK, // Successfully transacted. For Debit, SMS, P2P types only
    ERR, //Error occurred while transacting, see 'resp' for more info
    ROK, //Successfully reversed. For REVERSAL type only.
    RER, //Reversed with Error. For REVERSAL type only.
    NTF //Uncompleted transaction
}