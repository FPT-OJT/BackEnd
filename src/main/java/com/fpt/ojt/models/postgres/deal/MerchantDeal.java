package com.fpt.ojt.models.postgres.deal;

import com.fpt.ojt.models.postgres.AbstractBaseEntity;
import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "merchant_deals")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDeal extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_agency_id", nullable = false)
    private MerchantAgency merchantAgency;

    @Column(name = "deal_name")
    private String dealName;

    @Column(name = "discount_rate")
    private Double discountRate;

    @Column(name = "cashback_rate")
    private Double cashbackRate;

    @Column(name = "points_multiplier")
    private Double pointsMultiplier;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;
}
