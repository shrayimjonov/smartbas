package nci.billing.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    FAILED(514, "Something went wrong"),
    FAILED_PAY(505, "Payment Failed"),
    FAILED_CHECK(508, "Check Failed"),
    SUCCESS(200, "Success"),
    PENDING(202, "Transaction is in Pending status"),
    BANK_CLOSED(506, "Bank Day is closed"),
    REVERSE_FAILED(509, "Reverse failed"),
    DEBT_REPAY_FAILED(510, "Main Debt Repayment failed"),
    REVERSE_SUCCESS(200, "Success Reversed"),
    LOAN_NOT_FOUND(444, "Loan not found"),
    WRONG_REQUEST(445, "Something went wrong"),
    TRANSACTION_NOT_COMPLETED(446, "Third party not responding (NCI)"),
    REVERSE_NOT_COMPLETED(447, "Third party not responding (NCI)"),;

    private final int code;
    private final String message;

}
