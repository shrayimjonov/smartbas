package nci.billing.model.enums;

import lombok.Getter;

@Getter
public enum PayStatus {
    CREATED,
    PREPARE,
    SUCCESS,
    FAILED
}
