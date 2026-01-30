# Công Thức Tính Total Discount (Merchant Deal + Card Rules)

## Tổng Quan

Công thức tính `totalDiscount` cho mỗi merchant kết hợp 2 nguồn ưu đãi:
1. **Merchant Deal**: Ưu đãi từ merchant agency (discount_rate, cashback_rate, points_multiplier)
2. **Card Rules**: Ưu đãi từ thẻ tín dụng của user (effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate)

---

## Bước 1: Lọc Merchant Deals Hợp Lệ

### Điều Kiện Lọc

Một `merchant_deal` được coi là hợp lệ khi:

```
✓ valid_from <= TODAY <= valid_to
✓ deleted_at IS NULL
✓ (
    KHÔNG có entry nào trong card_merchant_deals  --> Universal Deal (áp dụng cho ALL cards)
    OR
    Có ít nhất 1 card của user trong card_merchant_deals AND card chưa hết hạn (expiry_date >= TODAY)
  )
```

### SQL Query Lọc Deals

```sql
SELECT DISTINCT md.*
FROM merchant_deals md
-- LEFT JOIN để bắt cả universal deals (không có card_merchant_deals)
LEFT JOIN card_merchant_deals cmd ON cmd.merchant_deal_id = md.id
LEFT JOIN user_credit_cards ucc ON ucc.card_product_id = cmd.card_product_id AND ucc.user_id = :userId
WHERE 
    md.valid_from <= CURRENT_DATE
    AND md.valid_to >= CURRENT_DATE
    AND md.deleted_at IS NULL
    AND (
        -- Case 1: Universal deal (không có card_merchant_deals)
        NOT EXISTS (
            SELECT 1 FROM card_merchant_deals cmd2 
            WHERE cmd2.merchant_deal_id = md.id
        )
        OR
        -- Case 2: User có ít nhất 1 card hợp lệ
        (ucc.id IS NOT NULL AND ucc.expiry_date >= CURRENT_DATE AND ucc.deleted_at IS NULL)
    )
```

---

## Bước 2: Lọc Card Rules Hợp Lệ (Không Cần Match Conditions)

### Điều Kiện Lọc Card Rules

Một `card_rule` được áp dụng tự động (không cần user input) khi:

```
✓ card_rule.card_product_id thuộc user_credit_cards của user
✓ user_credit_card.expiry_date >= TODAY
✓ card_rule.match_conditions IS NULL OR EMPTY  --> Không cần điều kiện động
✓ (
    (match_allow_mccs IS NULL AND match_reject_mccs IS NULL)  --> Áp dụng cho ALL merchants
    OR
    (match_allow_mccs CONTAINS merchant.mcc)  --> MCC merchant nằm trong whitelist
    OR
    (match_reject_mccs NOT CONTAINS merchant.mcc)  --> MCC merchant KHÔNG nằm trong blacklist
  )
```

### SQL Query Lọc Card Rules

```sql
SELECT cr.*
FROM card_rules cr
JOIN user_credit_cards ucc ON ucc.card_product_id = cr.card_product_id
JOIN merchants m ON m.id = :merchantId
WHERE 
    ucc.user_id = :userId
    AND ucc.expiry_date >= CURRENT_DATE
    AND ucc.deleted_at IS NULL
    AND cr.deleted_at IS NULL
    -- Chỉ lấy rules không có match_conditions (tự động áp dụng)
    AND (cr.match_conditions IS NULL OR jsonb_array_length(cr.match_conditions) = 0)
    -- Kiểm tra MCC
    AND (
        -- Case 1: Áp dụng cho ALL merchants
        (cr.match_allow_mccs IS NULL AND cr.match_reject_mccs IS NULL)
        OR
        -- Case 2: MCC trong whitelist
        (cr.match_allow_mccs IS NOT NULL AND m.mcc = ANY(SELECT jsonb_array_elements_text(cr.match_allow_mccs)))
        OR
        -- Case 3: MCC KHÔNG trong blacklist
        (cr.match_reject_mccs IS NOT NULL AND m.mcc <> ALL(SELECT jsonb_array_elements_text(cr.match_reject_mccs)))
    )
```

---

## Bước 2.1: Tính Total Discount Từ Card Rules

### Các Trường Effect Trong Card Rule

| Field | Ý Nghĩa | Đơn Vị |
|-------|---------|--------|
| `effect_rebate_rate` | % giảm trừ trực tiếp vào bill (áp dụng trước) | % |
| `effect_cashback_rate` | % hoàn tiền sau giao dịch (tính trên số tiền sau rebate) | % |
| `effect_merchant_discount_rate` | % merchant chia sẻ (merchant trả, user hưởng) | % |
| `effect_fee_rate` | % phí giao dịch (giảm lợi ích) | % |

### Công Thức Tính Card Rule Benefit

**Logic tính toán theo thứ tự:**

1. Tính tổng discount trực tiếp (rebate + merchant_discount):
   ```
   total_rebate = effect_rebate_rate + effect_merchant_discount_rate
   ```

2. Tính cashback (áp dụng sau khi đã rebate):
   ```
   effective_cashback = effect_cashback_rate × (1 - total_rebate / 100)
   ```

3. Trừ phí:
   ```
   card_rule_benefit = total_rebate + effective_cashback - effect_fee_rate
   ```

**Công thức gộp:**
```
card_rule_benefit = 
    (effect_rebate_rate + effect_merchant_discount_rate) +
    [effect_cashback_rate × (1 - (effect_rebate_rate + effect_merchant_discount_rate) / 100)] -
    effect_fee_rate
```

**Giải thích:**
- Rebate/Merchant Discount giảm giá trực tiếp → cộng thẳng
- Cashback tính trên số tiền **sau khi đã giảm giá** → nhân với `(1 - total_rebate/100)`
- Fee là chi phí → trừ đi

**Ví dụ:**
- `effect_rebate_rate` = 10%
- `effect_cashback_rate` = 5%
- `effect_fee_rate` = 1%

Tính:
```
total_rebate = 10 + 0 = 10%
effective_cashback = 5 × (1 - 10/100) = 5 × 0.9 = 4.5%
card_rule_benefit = 10 + 4.5 - 1 = 13.5%
```


**Lưu ý:**
- Nếu field là `NULL`, coi như `0`
- `effect_fee_rate` là chi phí → trừ đi
- Nếu user có **nhiều cards**, mỗi card có thể có **nhiều rules** hợp lệ cho cùng 1 merchant

### Cách Tổng Hợp Nhiều Rules

**Option 1: Cộng Dồn (Recommended)**
```
total_card_rules_benefit = SUM(card_rule_benefit for all eligible rules)
```

**Option 2: Lấy Max**
```
total_card_rules_benefit = MAX(card_rule_benefit for all eligible rules)
```

→ **Đề xuất dùng Option 1** vì thực tế các rules có thể stack (VD: cashback rule + points rule)

---

## Bước 3: Tính Total Discount Từ Merchant Deal

### Công Thức Merchant Deal Benefit

**Logic tính toán:**

1. Tính tổng discount trực tiếp:
   ```
   total_discount_rate = discount_rate
   ```

2. Tính cashback (áp dụng sau discount):
   ```
   effective_cashback = cashback_rate × (1 - discount_rate / 100)
   ```

3. Công thức gộp:
   ```
   merchant_deal_benefit = discount_rate + [cashback_rate × (1 - discount_rate / 100)]
   ```

**Giải thích:**
- `discount_rate`: Giảm giá trực tiếp vào bill
- `cashback_rate`: Hoàn tiền tính trên số tiền **sau khi đã giảm giá**
- `points_multiplier`: **Không tính vào totalDiscount** (vì điểm thưởng không phải lợi ích trực tiếp bằng tiền)

**Ví dụ:**
- `discount_rate` = 20%
- `cashback_rate` = 10%

Tính:
```
effective_cashback = 10 × (1 - 20/100) = 10 × 0.8 = 8%
merchant_deal_benefit = 20 + 8 = 28%
```

**Lưu ý:**
- Nếu field là `NULL`, coi như `0`

---

## Bước 4: Tổng Hợp Total Discount

### Công Thức Cuối Cùng

```
totalDiscount = merchant_deal_benefit + total_card_rules_benefit
```

### Ví Dụ Cụ Thể

#### Merchant Deal:
- discount_rate: 15%
- cashback_rate: 5%
- points_multiplier: 1.5x (không tính vào totalDiscount)

**Tính:**
```
effective_cashback = 5 × (1 - 15/100) = 5 × 0.85 = 4.25%
merchant_deal_benefit = 15 + 4.25 = 19.25%
```

#### Card Rules (User có 2 cards, 3 rules hợp lệ):

**Rule 1 (Card A - Dining):**
- effect_rebate_rate: 2%
- effect_cashback_rate: 3%

```
total_rebate = 2%
effective_cashback = 3 × (1 - 2/100) = 3 × 0.98 = 2.94%
rule_1_benefit = 2 + 2.94 = 4.94%
```

**Rule 2 (Card A - All Merchants):**
- effect_rebate_rate: 1%
- effect_fee_rate: 0.5%

```
rule_2_benefit = 1 - 0.5 = 0.5%
```

**Rule 3 (Card B - Dining MCC):**
- effect_merchant_discount_rate: 5%
- effect_cashback_rate: 2%

```
total_rebate = 5%
effective_cashback = 2 × (1 - 5/100) = 2 × 0.95 = 1.9%
rule_3_benefit = 5 + 1.9 = 6.9%
```

**Tổng card rules:**
```
total_card_rules_benefit = 4.94 + 0.5 + 6.9 = 12.34%
```

#### Total Discount:
```
totalDiscount = 19.25 + 12.34 = 31.59%
```

---

## Bước 5: Xếp Hạng Merchants

### Sắp Xếp

```
ORDER BY totalDiscount DESC
```

→ Merchant có `totalDiscount` cao nhất = "Deal ngon nhất"

---

## SQL Query Hoàn Chỉnh (Top N Merchants)

```sql
WITH user_valid_deals AS (
    -- Lọc merchant deals hợp lệ
    SELECT DISTINCT 
        m.id AS merchant_id,
        m.name AS merchant_name,
        m.logo_url AS image_url,
        m.mcc,
        md.deal_name AS merchant_deal_name,
        COALESCE(md.discount_rate, 0) AS discount_rate,
        COALESCE(md.cashback_rate, 0) AS cashback_rate,
        -- Merchant deal benefit: discount + cashback × (1 - discount/100)
        (COALESCE(md.discount_rate, 0) + 
         COALESCE(md.cashback_rate, 0) * (1 - COALESCE(md.discount_rate, 0) / 100.0)) AS merchant_deal_benefit
    FROM merchants m
    JOIN merchant_agencies ma ON ma.merchant_id = m.id
    JOIN merchant_deals md ON md.merchant_agency_id = ma.id
    LEFT JOIN card_merchant_deals cmd ON cmd.merchant_deal_id = md.id
    LEFT JOIN user_credit_cards ucc ON ucc.card_product_id = cmd.card_product_id AND ucc.user_id = :userId
    WHERE 
        md.valid_from <= CURRENT_DATE
        AND md.valid_to >= CURRENT_DATE
        AND md.deleted_at IS NULL
        AND m.deleted_at IS NULL
        AND (
            -- Universal deal
            NOT EXISTS (SELECT 1 FROM card_merchant_deals cmd2 WHERE cmd2.merchant_deal_id = md.id)
            OR
            -- User có card hợp lệ
            (ucc.id IS NOT NULL AND ucc.expiry_date >= CURRENT_DATE AND ucc.deleted_at IS NULL)
        )
),
user_card_rules_benefit AS (
    -- Tính tổng benefit từ card rules cho mỗi merchant
    SELECT 
        uvd.merchant_id,
        SUM(
            -- total_rebate = rebate + merchant_discount
            (COALESCE(cr.effect_rebate_rate, 0) + COALESCE(cr.effect_merchant_discount_rate, 0)) +
            -- effective_cashback = cashback × (1 - total_rebate/100)
            COALESCE(cr.effect_cashback_rate, 0) * (1 - (COALESCE(cr.effect_rebate_rate, 0) + COALESCE(cr.effect_merchant_discount_rate, 0)) / 100.0) -
            -- fee
            COALESCE(cr.effect_fee_rate, 0)
        ) AS card_rules_benefit
    FROM user_valid_deals uvd
    JOIN card_rules cr ON cr.deleted_at IS NULL
    JOIN user_credit_cards ucc ON ucc.card_product_id = cr.card_product_id 
                                AND ucc.user_id = :userId 
                                AND ucc.expiry_date >= CURRENT_DATE
                                AND ucc.deleted_at IS NULL
    WHERE 
        -- Chỉ lấy rules không có match_conditions
        (cr.match_conditions IS NULL OR jsonb_array_length(cr.match_conditions) = 0)
        -- Kiểm tra MCC
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
    -- Total discount = merchant deal benefit + card rules benefit
    (uvd.merchant_deal_benefit + COALESCE(ucrb.card_rules_benefit, 0)) AS total_discount,
    -- Debug info
    uvd.merchant_deal_benefit,
    COALESCE(ucrb.card_rules_benefit, 0) AS card_rules_benefit
FROM user_valid_deals uvd
LEFT JOIN user_card_rules_benefit ucrb ON ucrb.merchant_id = uvd.merchant_id
ORDER BY total_discount DESC
LIMIT :topN;
```

---

## Các Trường Hợp Đặc Biệt

### 1. User Không Có Card Nào
- `merchant_deal_benefit` = 0 (vì không có card → không có deal)
- `total_discount` = 0
- Không hiển thị merchant này

### 2. Merchant Có Deal Nhưng User Không Có Card Hợp Lệ
- Nếu là universal deal → vẫn hiển thị nếu user có ít nhất 1 card bất kỳ
- Nếu là specific card deal → không hiển thị

### 3. Merchant Không Có Deal Nhưng Card Rules Vẫn Áp Dụng
- `merchant_deal_benefit` = 0
- `card_rules_benefit` > 0
- `total_discount` = card_rules_benefit
- **Vẫn hiển thị** vì user vẫn có lợi từ card rules

### 4. Card Rule Có `effect_fee_rate` Cao
- Có thể làm `card_rules_benefit` âm
- Nếu `total_discount` < 0 → không hiển thị merchant này (lọc WHERE total_discount > 0)

---

## Java Implementation Pseudocode

```java
public class TotalDiscountCalculator {
    
    public static double calculateMerchantDealBenefit(MerchantDeal deal) {
        double discount = deal.getDiscountRate() != null ? deal.getDiscountRate() : 0.0;
        double cashback = deal.getCashbackRate() != null ? deal.getCashbackRate() : 0.0;
        
        // cashback tính trên số tiền sau khi đã giảm giá
        double effectiveCashback = cashback * (1 - discount / 100.0);
        
        return discount + effectiveCashback;
    }
    
    public static double calculateCardRuleBenefit(CardRule rule) {
        double rebate = rule.getEffectRebateRate() != null ? rule.getEffectRebateRate() : 0.0;
        double cashback = rule.getEffectCashbackRate() != null ? rule.getEffectCashbackRate() : 0.0;
        double merchantDiscount = rule.getEffectMerchantDiscountRate() != null ? rule.getEffectMerchantDiscountRate() : 0.0;
        double fee = rule.getEffectFeeRate() != null ? rule.getEffectFeeRate() : 0.0;
        
        // Tổng discount trực tiếp
        double totalRebate = rebate + merchantDiscount;
        
        // Cashback tính trên số tiền sau khi đã rebate
        double effectiveCashback = cashback * (1 - totalRebate / 100.0);
        
        return totalRebate + effectiveCashback - fee;
    }
    
    public static double calculateTotalDiscount(
        MerchantDeal deal, 
        List<CardRule> eligibleRules
    ) {
        double merchantBenefit = calculateMerchantDealBenefit(deal);
        double cardRulesBenefit = eligibleRules.stream()
            .mapToDouble(TotalDiscountCalculator::calculateCardRuleBenefit)
            .sum();
        
        return merchantBenefit + cardRulesBenefit;
    }
}
```

---

## Kết Luận

**Công thức tổng quát:**

```
totalDiscount = merchant_deal_benefit + card_rules_benefit

Trong đó:

merchant_deal_benefit = discount_rate + [cashback_rate × (1 - discount_rate / 100)]

card_rules_benefit = SUM(
    (effect_rebate_rate + effect_merchant_discount_rate) +
    [effect_cashback_rate × (1 - (effect_rebate_rate + effect_merchant_discount_rate) / 100)] -
    effect_fee_rate
)
```

**Giải thích:**
- Discount/Rebate giảm giá trực tiếp → cộng thẳng
- Cashback tính trên số tiền **sau khi đã giảm giá** → nhân với `(1 - discount/100)`
- Fee là chi phí → trừ đi
- Points **không tính** vào totalDiscount

**Điều kiện:**
- Merchant deal còn hiệu lực (valid_from ≤ TODAY ≤ valid_to)
- User có card hợp lệ cho deal (hoặc universal deal)
- Card rules không có match_conditions (tự động áp dụng)
- MCC của merchant khớp với allow/reject MCCs của rule
