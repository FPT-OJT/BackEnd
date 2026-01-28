package com.fpt.ojt.models.postgres.card;

import com.fpt.ojt.models.postgres.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "card_rules")
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CardRule extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_product_id", nullable = false)
    private CardProduct cardProduct;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "match_conditions")
    private List<MatchCondition> matchConditions;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "match_allow_mccs")
    private List<String> matchAllowMccs;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "match_reject_mccs")
    private List<String> matchRejectMccs;

    @Column(name = "effect_cashback_rate")
    private Double effectCashbackRate;

    @Column(name = "effect_points_rate")
    private Double effectPointsRate;

    @Column(name = "effect_rebate_rate")
    private Double effectRebateRate;

    @Column(name = "effect_merchant_discount_rate")
    private Double effectMerchantDiscountRate;

    @Column(name = "effect_fee_rate")
    private Double effectFeeRate;
}
