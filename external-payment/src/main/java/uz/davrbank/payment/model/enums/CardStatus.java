package uz.davrbank.payment.model.enums;

/**
 * Card status enum - matches UserCardStatus from card-service
 */
public enum CardStatus {
    PENDING,
    ACTIVE,
    INACTIVE,
    SERVICE_UNAVAILABLE,
    HARD_BLOCKED,
    SOFT_BLOCKED;

    public boolean isActiveAndNotBlocked() {
        return this == ACTIVE;
    }

    public boolean isBlocked() {
        return this == HARD_BLOCKED || this == SOFT_BLOCKED;
    }
}
