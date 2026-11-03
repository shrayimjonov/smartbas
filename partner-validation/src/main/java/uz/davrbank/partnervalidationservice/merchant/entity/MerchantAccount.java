package uz.davrbank.partnervalidationservice.merchant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.partnervalidationservice.common.entity.BaseEntity;
import uz.davrbank.partnervalidationservice.merchant.enums.AccountStatus;
import uz.davrbank.partnervalidationservice.merchant.enums.Currency;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "merchant_accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MerchantAccount extends BaseEntity {

    @Column(name = "merchant_id", nullable = false)
    UUID merchantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", updatable = false, insertable = false)
    Merchant merchant;

    @Column(name = "branch_code", nullable = false)
    String branchCode;

    @Column(name = "account_number", nullable = false)
    String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "currency")
    Currency currency;

    @Column(name = "abs_client_id")
    String absClientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    AccountStatus status;
}