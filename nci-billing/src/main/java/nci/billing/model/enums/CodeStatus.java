package nci.billing.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@Getter
@RequiredArgsConstructor
@ToString
public enum CodeStatus {
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    LOAN_NOT_FOUND(403, "Loan not found or not approved yet in Core Banking"),
    INVALID_PARAMS(405, "Required fields not presented or wrong filled"),
    ENTITY_NOT_FOUND(406, "Entity not found in database"),
    WRONG_REQUEST(407, "Http message not readable"),
    FRAUD_TRANSACTION(408, "This transaction blocked by Fraud policy"),

    INTERNAL_ERROR(500, "Internal Server Error"),
    TIME_OUT(504, "Gateway Time out"),
    SIMULATE_ERROR(506, "Error while simulate info");

    private final int code;
    private final String text;
}
