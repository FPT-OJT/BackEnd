# Công Thức Tính Total Discount - Phiên Bản Đơn Giản

## Công Thức Hoàn Chỉnh

### Bước 1: Tính Card Rules (cho từng rule)

1. **Tổng discount trực tiếp:**
   ```
   total_rebate = effect_rebate_rate + effect_merchant_discount_rate
   ```

2. **Effective cashback từ card rule:**
   ```
   effective_cashback_from_card = effect_cashback_rate × (1 - total_rebate / 100)
   ```

3. **Card rule benefit (chỉ tính discount và fee):**
   ```
   card_rule_benefit = total_rebate - effect_fee_rate
   ```

---

### Bước 2: Tổng hợp tất cả card rules

```
total_card_rule_benefit = SUM(card_rule_benefit for all eligible rules)

total_effective_cashback_from_cards = SUM(effective_cashback_from_card for all eligible rules)
```

---

### Bước 3: Kết hợp với Merchant Deal

1. **Tổng cashback:**
   ```
   total_cashback = total_effective_cashback_from_cards + merchant_deal.cashback_rate
   ```

2. **Tổng discount trực tiếp (merchant tính trước):**
   ```
   total_direct_discount = merchant_deal.discount_rate + 
                           total_card_rule_benefit × (1 - merchant_deal.discount_rate / 100)
   ```
   
   > **Giải thích:** Card benefit tính trên số tiền sau khi merchant đã giảm giá

3. **Total Discount cuối cùng:**
   ```
   totalDiscount = total_direct_discount + total_cashback
   ```

---

## Công Thức Gộp (1 dòng)

```
totalDiscount = 
    merchant_deal.discount_rate +
    SUM(card_rule_benefit) × (1 - merchant_deal.discount_rate / 100) +
    SUM(effective_cashback_from_card) + 
    merchant_deal.cashback_rate

Trong đó:
    card_rule_benefit = (effect_rebate_rate + effect_merchant_discount_rate) - effect_fee_rate
    
    effective_cashback_from_card = effect_cashback_rate × (1 - (effect_rebate_rate + effect_merchant_discount_rate) / 100)
```

---

## Ví Dụ Cụ Thể

### Input:

**Merchant Deal:**
- discount_rate = 20%
- cashback_rate = 5%

**Card Rules:**

**Rule 1:**
- effect_rebate_rate = 3%
- effect_cashback_rate = 2%
- effect_fee_rate = 0.5%

**Rule 2:**
- effect_merchant_discount_rate = 5%
- effect_cashback_rate = 4%

---

### Tính toán từng bước:

**Rule 1:**
```
total_rebate = 3%
effective_cashback_from_card = 2 × (1 - 3/100) = 1.94%
card_rule_benefit = 3 - 0.5 = 2.5%
```

**Rule 2:**
```
total_rebate = 5%
effective_cashback_from_card = 4 × (1 - 5/100) = 3.8%
card_rule_benefit = 5 - 0 = 5%
```

**Tổng hợp card rules:**
```
total_card_rule_benefit = 2.5 + 5 = 7.5%
total_effective_cashback_from_cards = 1.94 + 3.8 = 5.74%
```

**Kết hợp với merchant:**
```
total_cashback = 5.74 + 5 = 10.74%

total_direct_discount = 20 + 7.5 × (1 - 20/100)
                      = 20 + 7.5 × 0.8
                      = 20 + 6
                      = 26%

totalDiscount = 26 + 10.74 = 36.74%
```

---

## SQL Implementation

```sql
WITH user_card_rules_detail AS (
    SELECT 
        uvd.merchant_id,
        -- Card rule benefit (không bao gồm cashback)
        SUM(
            (COALESCE(cr.effect_rebate_rate, 0) + COALESCE(cr.effect_merchant_discount_rate, 0)) -
            COALESCE(cr.effect_fee_rate, 0)
        ) AS total_card_rule_benefit,
        -- Effective cashback từ card rules
        SUM(
            COALESCE(cr.effect_cashback_rate, 0) * 
            (1 - (COALESCE(cr.effect_rebate_rate, 0) + COALESCE(cr.effect_merchant_discount_rate, 0)) / 100.0)
        ) AS total_effective_cashback_from_cards
    FROM user_valid_deals uvd
    JOIN card_rules cr ON cr.deleted_at IS NULL
    JOIN user_credit_cards ucc ON ucc.card_product_id = cr.card_product_id 
                                AND ucc.user_id = :userId 
                                AND ucc.expiry_date >= CURRENT_DATE
                                AND ucc.deleted_at IS NULL
    WHERE 
        (cr.match_conditions IS NULL OR jsonb_array_length(cr.match_conditions) = 0)
        AND (
            (cr.match_allow_mccs IS NULL AND cr.match_reject_mccs IS NULL)
            OR
            (cr.match_allow_mccs IS NOT NULL AND uvd.mcc = ANY(SELECT jsonb_array_elements_text(cr.match_allow_mccs)))
            OR
            (cr.match_reject_mccs IS NOT NULL AND uvd.mcc <> ALL(SELECT jsonb_array_elements_text(cr.match_reject_mccs)))
        )
    GROUP BY uvd.merchant_id
)
SELECT 
    uvd.merchant_id,
    uvd.merchant_name,
    uvd.image_url,
    uvd.merchant_deal_name,
    -- Total discount
    (
        -- Merchant discount trước
        COALESCE(uvd.discount_rate, 0) +
        -- Card rules benefit (áp dụng trên số tiền sau merchant discount)
        COALESCE(ucrd.total_card_rule_benefit, 0) * (1 - COALESCE(uvd.discount_rate, 0) / 100.0) +
        -- Total cashback
        COALESCE(ucrd.total_effective_cashback_from_cards, 0) + COALESCE(uvd.cashback_rate, 0)
    ) AS total_discount
FROM user_valid_deals uvd
LEFT JOIN user_card_rules_detail ucrd ON ucrd.merchant_id = uvd.merchant_id
ORDER BY total_discount DESC
LIMIT :topN;
```

---

## Java Implementation

```java
public class TotalDiscountCalculator {
    
    public static double calculateTotalDiscount(
        MerchantDeal merchantDeal,
        List<CardRule> eligibleRules
    ) {
        double merchantDiscount = merchantDeal.getDiscountRate() != null ? merchantDeal.getDiscountRate() : 0.0;
        double merchantCashback = merchantDeal.getCashbackRate() != null ? merchantDeal.getCashbackRate() : 0.0;
        
        // Bước 1 & 2: Tính từ card rules
        double totalCardRuleBenefit = 0.0;
        double totalEffectiveCashbackFromCards = 0.0;
        
        for (CardRule rule : eligibleRules) {
            double rebate = rule.getEffectRebateRate() != null ? rule.getEffectRebateRate() : 0.0;
            double merchantDiscountRate = rule.getEffectMerchantDiscountRate() != null ? rule.getEffectMerchantDiscountRate() : 0.0;
            double cashback = rule.getEffectCashbackRate() != null ? rule.getEffectCashbackRate() : 0.0;
            double fee = rule.getEffectFeeRate() != null ? rule.getEffectFeeRate() : 0.0;
            
            double totalRebate = rebate + merchantDiscountRate;
            double effectiveCashback = cashback * (1 - totalRebate / 100.0);
            double cardRuleBenefit = totalRebate - fee;
            
            totalCardRuleBenefit += cardRuleBenefit;
            totalEffectiveCashbackFromCards += effectiveCashback;
        }
        
        // Bước 3: Kết hợp với merchant deal
        double totalCashback = totalEffectiveCashbackFromCards + merchantCashback;
        double totalDirectDiscount = merchantDiscount + totalCardRuleBenefit * (1 - merchantDiscount / 100.0);
        
        return totalDirectDiscount + totalCashback;
    }
}
```

---

## Tóm Tắt

**Thứ tự tính:**
1. Merchant giảm giá trước
2. Card rules áp dụng trên số tiền đã giảm
3. Cashback cộng tất cả vào cuối

**Lưu ý:**
- Cashback luôn tính trên số tiền sau khi đã giảm giá
- Card rule benefit không bao gồm cashback (tách riêng để tính sau)
- Fee trừ trực tiếp vào card rule benefit
