package nci.billing.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SystemType {
    PAYMO("Paymo integrated with SVGATE and HUMO", 3636),
    GENESIS("UZCARD processing repayment loan system", 3637);

    private final String name;
    private final Integer number;

}
