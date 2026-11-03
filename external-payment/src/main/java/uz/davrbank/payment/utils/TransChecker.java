package uz.davrbank.payment.utils;

import lombok.extern.slf4j.Slf4j;
import uz.davrbank.payment.model.entity.Transaction;
import uz.nodir.commondto.exception.InvalidDataException;
import uz.nodir.commondto.utils.SecurityUtils;

@Slf4j
public class TransChecker {

    public static void checkUserToTransaction(Transaction transaction) {
        var username = SecurityUtils.getCurrentUserName();
        log.info("Current user is: {}", username);

        if (isAdmin()) {
            log.info("Admin user {} accessing transaction owned by {}", username, transaction.getCreatedBy());
            return;
        }

        if (!transaction.getCreatedBy().equals(username)) {
            log.error("Current user is: {} but transaction owner is: {}", username, transaction.getCreatedBy());
            throw new InvalidDataException("Transaction not found");
        }
    }

    public static boolean isAdmin() {
        var currentUser = SecurityUtils.getCurrentUser();
        return currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
