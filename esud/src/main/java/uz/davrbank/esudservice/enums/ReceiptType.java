package uz.davrbank.esudservice.enums;

import lombok.Getter;
import uz.davrbank.esudservice.exception.CustomException;

import java.util.Arrays;

@Getter
public enum ReceiptType {
    POST(3),
    STATE(1),
    VCC(4),
    DAMAGE(5),
    FINE(2);

    private final int payCategoryId;

    ReceiptType(final int payCategoryId) {
        this.payCategoryId = payCategoryId;
    }

    public static ReceiptType convertPayCategoryIdToType(int payCategoryId) {
        return Arrays.stream(values())
                .filter(receiptType -> receiptType.payCategoryId == payCategoryId)
                .findFirst()
                .orElseThrow(() -> new CustomException("Receipt type not found for given pay category id: " + payCategoryId));
    }
}
