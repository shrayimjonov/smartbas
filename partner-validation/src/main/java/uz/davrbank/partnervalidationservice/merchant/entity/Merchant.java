package uz.davrbank.partnervalidationservice.merchant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import uz.davrbank.partnervalidationservice.common.entity.BaseEntity;
import uz.davrbank.partnervalidationservice.merchant.enums.MerchantStatus;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "merchants")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Merchant extends BaseEntity {

    @Column(name = "merchant_external_id", nullable = false, unique = true)
    String merchantExternalId;

    @Column(nullable = false, name = "name")
    String name;

    @Column(nullable = false, name = "tin")
    String tin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    MerchantStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "allowed_operations", nullable = false, columnDefinition = "jsonb")
    LinkedHashSet<String> allowedOperations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "merchant")
    List<MerchantAccount> accounts = new ArrayList<>();
}
