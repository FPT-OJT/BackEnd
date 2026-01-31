-- =====================================================
-- Flyway Migration V1.6: Insert Card Rules
-- Description: Insert reward/fee rules for credit cards with proper match conditions
-- =====================================================

-- =====================================================
-- DBS Altitude Visa Card Rules
-- =====================================================

-- Rule 1: DBS Altitude - Premium Dining Rewards (Weekend)
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '751c0c56-40e2-4dd1-a259-e4bca5b3cf65', -- DBS Altitude Visa
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["DINING", "ENTERTAINMENT"],
            "rejectedValues": ["UTILITIES", "INSURANCE", "CASH_ADVANCE"]
        },
        {
            "conditionCode": "MIN_TRANSACTION_AMOUNT",
            "acceptedValues": ["50.00"],
            "rejectedValues": null
        },
        {
            "conditionCode": "DAY_OF_WEEK",
            "acceptedValues": ["WEEKEND", "FRIDAY"],
            "rejectedValues": null
        },
        {
            "conditionCode": "PAYMENT_METHOD",
            "acceptedValues": ["CONTACTLESS", "MOBILE_WALLET"],
            "rejectedValues": ["CASH_ADVANCE"]
        }
    ]'::jsonb,
    '["5812", "5814", "7832"]'::jsonb,
    '["6211", "7995"]'::jsonb,
    0.08, -- 8% cashback
    3.0,  -- 3 miles per SGD
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 2: DBS Altitude - Travel Rewards
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '751c0c56-40e2-4dd1-a259-e4bca5b3cf65',
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["TRAVEL"],
            "rejectedValues": null
        },
        {
            "conditionCode": "TRANSACTION_TYPE",
            "acceptedValues": ["PURCHASE"],
            "rejectedValues": ["CASH_ADVANCE", "BALANCE_TRANSFER"]
        }
    ]'::jsonb,
    '["3000", "3001", "3002", "3003", "3004", "3005", "3501", "3502", "3503"]'::jsonb,
    NULL,
    NULL,
    5.0, -- 5 miles per SGD for travel
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- Citi Rewards Card Rules
-- =====================================================

-- Rule 3: Citi Rewards - Online Shopping Cashback
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    'f6c123a3-86da-4208-addb-6b859a1c16c7', -- Citi Rewards Card
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["ONLINE_SHOPPING"],
            "rejectedValues": null
        },
        {
            "conditionCode": "IS_ONLINE_TRANSACTION",
            "acceptedValues": ["true"],
            "rejectedValues": null
        },
        {
            "conditionCode": "PAYMENT_METHOD",
            "acceptedValues": ["ONLINE", "MOBILE_WALLET"],
            "rejectedValues": null
        }
    ]'::jsonb,
    NULL,
    '["6211", "7995", "9211"]'::jsonb,
    0.10, -- 10% cashback
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 4: Citi Rewards - Department Store Bonus
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    'f6c123a3-86da-4208-addb-6b859a1c16c7',
    '[
        {
            "conditionCode": "MERCHANT_CATEGORY",
            "acceptedValues": ["RETAIL"],
            "rejectedValues": null
        },
        {
            "conditionCode": "MIN_TRANSACTION_AMOUNT",
            "acceptedValues": ["100.00"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["5311", "5651", "5691", "5999"]'::jsonb,
    NULL,
    NULL,
    4.0, -- 4 points per SGD
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- Citi Cashback Card Rules
-- =====================================================

-- Rule 5: Citi Cashback - Groceries & Petrol
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '60cadce1-8402-4a76-8b14-6e690f89b6d1', -- Citi Cashback Card
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["GROCERIES", "PETROL"],
            "rejectedValues": ["UTILITIES", "INSURANCE"]
        },
        {
            "conditionCode": "TRANSACTION_LOCATION",
            "acceptedValues": ["DOMESTIC"],
            "rejectedValues": ["INTERNATIONAL"]
        },
        {
            "conditionCode": "MONTHLY_SPENDING_THRESHOLD",
            "acceptedValues": ["500.00"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["5411", "5541", "5542"]'::jsonb,
    NULL,
    0.06, -- 6% cashback
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 6: Citi Cashback - All Other Spending
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '60cadce1-8402-4a76-8b14-6e690f89b6d1',
    '[
        {
            "conditionCode": "TRANSACTION_TYPE",
            "acceptedValues": ["PURCHASE"],
            "rejectedValues": ["CASH_ADVANCE", "BALANCE_TRANSFER", "INSTALLMENT"]
        }
    ]'::jsonb,
    NULL,
    '["6211", "7995"]'::jsonb,
    0.01, -- 1% base cashback
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- AMEX Platinum Credit Card Rules
-- =====================================================

-- Rule 7: AMEX Platinum - Premium Dining
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '20d26838-ba7d-446a-a33c-516bf2324234', -- AMEX Platinum Card
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["DINING"],
            "rejectedValues": null
        },
        {
            "conditionCode": "CUSTOMER_TIER",
            "acceptedValues": ["PLATINUM", "DIAMOND", "VIP"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["5812", "5814"]'::jsonb,
    NULL,
    NULL,
    10.0, -- 10 points per SGD
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 8: AMEX Platinum - Foreign Transaction Fee
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '20d26838-ba7d-446a-a33c-516bf2324234',
    '[
        {
            "conditionCode": "IS_FOREIGN_TRANSACTION",
            "acceptedValues": ["true"],
            "rejectedValues": null
        },
        {
            "conditionCode": "TRANSACTION_LOCATION",
            "acceptedValues": ["INTERNATIONAL"],
            "rejectedValues": null
        }
    ]'::jsonb,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    0.025, -- 2.5% foreign transaction fee
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- UOB One Card Rules
-- =====================================================

-- Rule 9: UOB One - Monthly Spending Rewards
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '10b0609e-c7b9-4cc2-bc93-004351861f7b', -- UOB One Card
    '[
        {
            "conditionCode": "MONTHLY_SPENDING_THRESHOLD",
            "acceptedValues": ["1000.00"],
            "rejectedValues": null
        },
        {
            "conditionCode": "TRANSACTION_COUNT_MONTHLY",
            "acceptedValues": ["5"],
            "rejectedValues": null
        },
        {
            "conditionCode": "HAS_AUTO_PAYMENT",
            "acceptedValues": ["true"],
            "rejectedValues": null
        }
    ]'::jsonb,
    NULL,
    '["6211", "7995"]'::jsonb,
    0.033, -- 3.33% cashback
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- OCBC 365 Credit Card Rules
-- =====================================================

-- Rule 10: OCBC 365 - Dining Cashback
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '0d8135c5-7187-4feb-95ca-65f443960e76', -- OCBC 365 Card
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["DINING"],
            "rejectedValues": null
        },
        {
            "conditionCode": "MIN_TRANSACTION_AMOUNT",
            "acceptedValues": ["20.00"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["5812", "5814"]'::jsonb,
    NULL,
    0.06, -- 6% cashback
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 11: OCBC 365 - Groceries Cashback
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '0d8135c5-7187-4feb-95ca-65f443960e76',
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["GROCERIES"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["5411", "5499"]'::jsonb,
    NULL,
    0.03, -- 3% cashback
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 12: OCBC 365 - Petrol Cashback
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '0d8135c5-7187-4feb-95ca-65f443960e76',
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["PETROL"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["5541", "5542"]'::jsonb,
    NULL,
    0.05, -- 5% cashback
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- GrabPay Card Rules
-- =====================================================

-- Rule 13: GrabPay - Grab Services Bonus
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    'e77ae6a5-7e5d-4444-9ae8-593017699487', -- GrabPay Card
    '[
        {
            "conditionCode": "BRAND_PARTNER",
            "acceptedValues": ["GRAB"],
            "rejectedValues": null
        },
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["TRANSPORT", "DINING"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["4121", "5812", "5814"]'::jsonb,
    NULL,
    0.20, -- 20% cashback for Grab
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 14: GrabPay - General Spending
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    'e77ae6a5-7e5d-4444-9ae8-593017699487',
    '[
        {
            "conditionCode": "TRANSACTION_TYPE",
            "acceptedValues": ["PURCHASE"],
            "rejectedValues": ["CASH_ADVANCE"]
        }
    ]'::jsonb,
    NULL,
    '["6211", "7995"]'::jsonb,
    0.01, -- 1% base cashback
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- HSBC TravelOne Card Rules
-- =====================================================

-- Rule 15: HSBC TravelOne - Overseas Spending
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '9873d4fb-04a0-43d6-b1aa-12b4d5117163', -- HSBC TravelOne Card
    '[
        {
            "conditionCode": "IS_FOREIGN_TRANSACTION",
            "acceptedValues": ["true"],
            "rejectedValues": null
        },
        {
            "conditionCode": "CURRENCY",
            "acceptedValues": ["USD", "EUR", "GBP", "JPY", "AUD"],
            "rejectedValues": ["SGD"]
        }
    ]'::jsonb,
    NULL,
    NULL,
    NULL,
    4.0, -- 4 miles per SGD overseas
    NULL,
    NULL,
    0.00, -- No foreign transaction fee
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 16: HSBC TravelOne - Travel Booking
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '9873d4fb-04a0-43d6-b1aa-12b4d5117163',
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["TRAVEL"],
            "rejectedValues": null
        },
        {
            "conditionCode": "MERCHANT_CATEGORY",
            "acceptedValues": ["HOSPITALITY"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["3000", "3001", "3002", "3003", "3501", "3502", "3503", "4511"]'::jsonb,
    NULL,
    NULL,
    6.0, -- 6 miles per SGD for travel
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- Standard Chartered Simply Cash Rules
-- =====================================================

-- Rule 17: SC Simply Cash - Unlimited Cashback
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '904d6105-f39a-4918-ab66-431217255d8e', -- SC Simply Cash
    '[
        {
            "conditionCode": "TRANSACTION_TYPE",
            "acceptedValues": ["PURCHASE"],
            "rejectedValues": ["CASH_ADVANCE", "BALANCE_TRANSFER"]
        },
        {
            "conditionCode": "MIN_TRANSACTION_AMOUNT",
            "acceptedValues": ["10.00"],
            "rejectedValues": null
        }
    ]'::jsonb,
    NULL,
    '["6211", "7995", "9211"]'::jsonb,
    0.015, -- 1.5% unlimited cashback
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- Maybank Family & Friends Card Rules
-- =====================================================

-- Rule 18: Maybank Family - Weekend Dining
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    'fd1f1e88-fc9c-470f-a0c2-21adbb3a8cab', -- Maybank Family Card
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["DINING"],
            "rejectedValues": null
        },
        {
            "conditionCode": "IS_WEEKEND",
            "acceptedValues": ["true"],
            "rejectedValues": null
        },
        {
            "conditionCode": "MIN_TRANSACTION_AMOUNT",
            "acceptedValues": ["30.00"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["5812", "5814"]'::jsonb,
    NULL,
    0.08, -- 8% cashback on weekends
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- BOC Sheng Siong Card Rules
-- =====================================================

-- Rule 19: BOC Sheng Siong - Supermarket Bonus
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '75698036-81d1-493c-8557-ce034112f5cb', -- BOC Sheng Siong Card
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["GROCERIES"],
            "rejectedValues": null
        },
        {
            "conditionCode": "MERCHANT_NAME",
            "acceptedValues": ["FAIRPRICE", "COLD_STORAGE", "SHENG_SIONG"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["5411", "5499"]'::jsonb,
    NULL,
    0.05, -- 5% cashback at supermarkets
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- AMEX True Cashback Rules
-- =====================================================

-- Rule 20: AMEX True - High Cashback (Local Spending)
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '8537e696-9000-4600-a2e2-259cce6effcf', -- AMEX True Cashback
    '[
        {
            "conditionCode": "TRANSACTION_LOCATION",
            "acceptedValues": ["DOMESTIC"],
            "rejectedValues": ["INTERNATIONAL"]
        },
        {
            "conditionCode": "MONTHLY_SPENDING_THRESHOLD",
            "acceptedValues": ["500.00"],
            "rejectedValues": null
        }
    ]'::jsonb,
    NULL,
    '["6211", "7995"]'::jsonb,
    0.015, -- 1.5% cashback domestic
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 21: AMEX True - International Spending
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '8537e696-9000-4600-a2e2-259cce6effcf',
    '[
        {
            "conditionCode": "TRANSACTION_LOCATION",
            "acceptedValues": ["INTERNATIONAL"],
            "rejectedValues": null
        }
    ]'::jsonb,
    NULL,
    NULL,
    0.03, -- 3% cashback international
    NULL,
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- Citi Premier Miles Card Rules
-- =====================================================

-- Rule 22: Citi Premier - Travel & Dining Miles
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    'c58da6a8-5651-43eb-8c71-7feb459d2a54', -- Citi Premier Miles
    '[
        {
            "conditionCode": "SPENDING_CATEGORY",
            "acceptedValues": ["TRAVEL", "DINING"],
            "rejectedValues": null
        }
    ]'::jsonb,
    '["3000", "3001", "3002", "3501", "3502", "5812", "5814"]'::jsonb,
    NULL,
    NULL,
    2.0, -- 2 miles per SGD
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 23: Citi Premier - General Spending Miles
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    'c58da6a8-5651-43eb-8c71-7feb459d2a54',
    '[
        {
            "conditionCode": "TRANSACTION_TYPE",
            "acceptedValues": ["PURCHASE"],
            "rejectedValues": ["CASH_ADVANCE", "BALANCE_TRANSFER"]
        }
    ]'::jsonb,
    NULL,
    '["6211", "7995"]'::jsonb,
    NULL,
    1.2, -- 1.2 miles per SGD base
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- =====================================================
-- UOB PRVI Miles Card Rules
-- =====================================================

-- Rule 24: UOB PRVI - Online & Foreign Spending
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '3baa0f89-afed-4237-8d66-ad7895c30501', -- UOB PRVI Miles
    '[
        {
            "conditionCode": "IS_ONLINE_TRANSACTION",
            "acceptedValues": ["true"],
            "rejectedValues": null
        }
    ]'::jsonb,
    NULL,
    NULL,
    NULL,
    2.4, -- 2.4 miles per SGD online
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Rule 25: UOB PRVI - Foreign Currency Spending
INSERT INTO card_rules (id, card_product_id, match_conditions, match_allow_mccs, match_reject_mccs, effect_cashback_rate, effect_points_rate, effect_rebate_rate, effect_merchant_discount_rate, effect_fee_rate, created_at, updated_at, deleted_at)
VALUES (
    gen_random_uuid(),
    '3baa0f89-afed-4237-8d66-ad7895c30501',
    '[
        {
            "conditionCode": "IS_FOREIGN_TRANSACTION",
            "acceptedValues": ["true"],
            "rejectedValues": null
        }
    ]'::jsonb,
    NULL,
    NULL,
    NULL,
    2.4, -- 2.4 miles per SGD foreign
    NULL,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);
