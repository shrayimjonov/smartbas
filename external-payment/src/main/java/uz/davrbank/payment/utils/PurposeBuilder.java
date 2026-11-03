package uz.davrbank.payment.utils;

import uz.davrbank.payment.model.entity.Transaction;
import uz.nodir.commondto.utils.SecurityUtils;

public class PurposeBuilder {

    public static String defaultBuild(Transaction transaction) {
        return "Transaction from %s with amount: %d to card: %s from payment: %s".formatted(
                SecurityUtils.getCurrentUserName(),
                transaction.getAmount().longValue() / 100,
                transaction.getAccount(),
                transaction.getPaymentId()
        );
    }
}
