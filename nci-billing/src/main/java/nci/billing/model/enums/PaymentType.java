package nci.billing.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    UZCARD(2),
    HUMO(4);
    private final int paymentType;

}
