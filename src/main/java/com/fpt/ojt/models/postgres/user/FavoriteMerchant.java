package com.fpt.ojt.models.postgres.user;

import com.fpt.ojt.models.postgres.AbstractBaseEntity;
import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_merchants")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteMerchant extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_agency_id", nullable = false)
    private MerchantAgency merchantAgency;
}
