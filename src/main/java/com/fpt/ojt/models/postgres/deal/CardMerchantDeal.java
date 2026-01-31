package com.fpt.ojt.models.postgres.deal;

import com.fpt.ojt.models.postgres.AbstractBaseEntity;
import com.fpt.ojt.models.postgres.card.CardProduct;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "card_merchant_deals")
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CardMerchantDeal extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_product_id", nullable = false)
    private CardProduct cardProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_deal_id", nullable = false)
    private MerchantDeal merchantDeal;
}
