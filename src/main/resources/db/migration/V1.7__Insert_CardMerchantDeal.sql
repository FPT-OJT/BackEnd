-- =====================================================
-- Flyway Migration V1.7: Insert Card Merchant Deals
-- Description: Link credit cards to merchant deals (which cards can participate in which deals)
-- =====================================================

-- =====================================================
-- Starbucks Deals
-- =====================================================

-- Deal 1: Starbucks New World - Buy 1 Get 1 Coffee
-- Popular cards accepted
INSERT INTO card_merchant_deals (id, card_product_id, merchant_deal_id, created_at, updated_at, deleted_at)
VALUES
-- DBS Altitude Visa
(gen_random_uuid(), '751c0c56-40e2-4dd1-a259-e4bca5b3cf65', '019c02b5-e5cd-719e-8412-ce5fe28dc468', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
-- Citi Rewards
(gen_random_uuid(), 'f6c123a3-86da-4208-addb-6b859a1c16c7', '019c02b5-e5cd-719e-8412-ce5fe28dc468', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
-- AMEX Platinum
(gen_random_uuid(), '20d26838-ba7d-446a-a33c-516bf2324234', '019c02b5-e5cd-719e-8412-ce5fe28dc468', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
-- OCBC 365
(gen_random_uuid(), '0d8135c5-7187-4feb-95ca-65f443960e76', '019c02b5-e5cd-719e-8412-ce5fe28dc468', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
-- UOB One Card
(gen_random_uuid(), '10b0609e-c7b9-4cc2-bc93-004351861f7b', '019c02b5-e5cd-719e-8412-ce5fe28dc468', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
-- Citi Cashback
(gen_random_uuid(), '60cadce1-8402-4a76-8b14-6e690f89b6d1', '019c02b5-e5cd-719e-8412-ce5fe28dc468', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- Deal 2: Starbucks Rex Hotel - Morning Coffee Discount
INSERT INTO card_merchant_deals (id, card_product_id, merchant_deal_id, created_at, updated_at, deleted_at)
VALUES
(gen_random_uuid(), '751c0c56-40e2-4dd1-a259-e4bca5b3cf65', '019c02b5-e5cd-719e-8412-ce5fe28dc479', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'f6c123a3-86da-4208-addb-6b859a1c16c7', '019c02b5-e5cd-719e-8412-ce5fe28dc479', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '0d8135c5-7187-4feb-95ca-65f443960e76', '019c02b5-e5cd-719e-8412-ce5fe28dc479', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c58da6a8-5651-43eb-8c71-7feb459d2a54', '019c02b5-e5cd-719e-8412-ce5fe28dc479', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '8537e696-9000-4600-a2e2-259cce6effcf', '019c02b5-e5cd-719e-8412-ce5fe28dc479', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- =====================================================
-- Haidilao Hotpot Deals
-- =====================================================

-- Deal 3: Haidilao Vincom Center - Hotpot Cashback 10%
INSERT INTO card_merchant_deals (id, card_product_id, merchant_deal_id, created_at, updated_at, deleted_at)
VALUES
-- Premium dining cards
(gen_random_uuid(), '751c0c56-40e2-4dd1-a259-e4bca5b3cf65', '019c02b5-e5cd-719e-8412-ce5fe28dc490', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '20d26838-ba7d-446a-a33c-516bf2324234', '019c02b5-e5cd-719e-8412-ce5fe28dc490', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'da60c099-8fb7-46c3-8182-ef077210fcb3', '019c02b5-e5cd-719e-8412-ce5fe28dc490', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- Citi Prestige
(gen_random_uuid(), '0d8135c5-7187-4feb-95ca-65f443960e76', '019c02b5-e5cd-719e-8412-ce5fe28dc490', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'f6c123a3-86da-4208-addb-6b859a1c16c7', '019c02b5-e5cd-719e-8412-ce5fe28dc490', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '60cadce1-8402-4a76-8b14-6e690f89b6d1', '019c02b5-e5cd-719e-8412-ce5fe28dc490', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'fd1f1e88-fc9c-470f-a0c2-21adbb3a8cab', '019c02b5-e5cd-719e-8412-ce5fe28dc490', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL); -- Maybank Family

-- =====================================================
-- Uniqlo Deals
-- =====================================================

-- Deal 4: Uniqlo Dong Khoi - Season Sale 30%
INSERT INTO card_merchant_deals (id, card_product_id, merchant_deal_id, created_at, updated_at, deleted_at)
VALUES
-- Shopping/Fashion cards
(gen_random_uuid(), 'f6c123a3-86da-4208-addb-6b859a1c16c7', '019c02b5-e5cd-719e-8412-ce5fe28dc781', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '60cadce1-8402-4a76-8b14-6e690f89b6d1', '019c02b5-e5cd-719e-8412-ce5fe28dc781', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '10b0609e-c7b9-4cc2-bc93-004351861f7b', '019c02b5-e5cd-719e-8412-ce5fe28dc781', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '904d6105-f39a-4918-ab66-431217255d8e', '019c02b5-e5cd-719e-8412-ce5fe28dc781', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- SC Simply Cash
(gen_random_uuid(), '55efeb5d-87ae-4bb5-a01c-506bbd8981c3', '019c02b5-e5cd-719e-8412-ce5fe28dc781', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- UOB Lady
(gen_random_uuid(), '8537e696-9000-4600-a2e2-259cce6effcf', '019c02b5-e5cd-719e-8412-ce5fe28dc781', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- =====================================================
-- Zara Deals
-- =====================================================

-- Deal 5: Zara Vincom Center - Member Exclusive 15%
INSERT INTO card_merchant_deals (id, card_product_id, merchant_deal_id, created_at, updated_at, deleted_at)
VALUES
-- Premium/Fashion cards
(gen_random_uuid(), '20d26838-ba7d-446a-a33c-516bf2324234', '019c02b5-e5cd-719e-8412-ce5fe28dc112', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'da60c099-8fb7-46c3-8182-ef077210fcb3', '019c02b5-e5cd-719e-8412-ce5fe28dc112', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'f6c123a3-86da-4208-addb-6b859a1c16c7', '019c02b5-e5cd-719e-8412-ce5fe28dc112', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '0418caa3-fc9b-4909-8e8f-0b4f4652cc81', '019c02b5-e5cd-719e-8412-ce5fe28dc112', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- Maybank Visa Infinite
(gen_random_uuid(), '6537851c-0b80-4a2b-915c-6d2880d9b30b', '019c02b5-e5cd-719e-8412-ce5fe28dc112', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL); -- UOB Signature

-- =====================================================
-- 7-Eleven Deals
-- =====================================================

-- Deal 6: 7-Eleven Saigon Trade Center - Daily Cashback 5%
INSERT INTO card_merchant_deals (id, card_product_id, merchant_deal_id, created_at, updated_at, deleted_at)
VALUES
-- General purpose cashback cards
(gen_random_uuid(), '60cadce1-8402-4a76-8b14-6e690f89b6d1', '019c02b5-e5cd-719e-8412-ce5fe28dc128', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '92255846-351a-4d69-a41e-c7d65adba916', '019c02b5-e5cd-719e-8412-ce5fe28dc128', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- Citi Cash Back+
(gen_random_uuid(), '8537e696-9000-4600-a2e2-259cce6effcf', '019c02b5-e5cd-719e-8412-ce5fe28dc128', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '904d6105-f39a-4918-ab66-431217255d8e', '019c02b5-e5cd-719e-8412-ce5fe28dc128', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '10b0609e-c7b9-4cc2-bc93-004351861f7b', '019c02b5-e5cd-719e-8412-ce5fe28dc128', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '0d8135c5-7187-4feb-95ca-65f443960e76', '019c02b5-e5cd-719e-8412-ce5fe28dc128', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'a3485870-7da5-4615-a186-7c95eb83f5dd', '019c02b5-e5cd-719e-8412-ce5fe28dc128', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- UOB Absolute Cashback
(gen_random_uuid(), 'e77ae6a5-7e5d-4444-9ae8-593017699487', '019c02b5-e5cd-719e-8412-ce5fe28dc128', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL); -- GrabPay Card

-- Deal 7: 7-Eleven Millennium - Points Booster 2x
INSERT INTO card_merchant_deals (id, card_product_id, merchant_deal_id, created_at, updated_at, deleted_at)
VALUES
-- Points/Miles cards
(gen_random_uuid(), 'f6c123a3-86da-4208-addb-6b859a1c16c7', '019c02b5-e5cd-719e-8412-ce5fe28dc938', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c58da6a8-5651-43eb-8c71-7feb459d2a54', '019c02b5-e5cd-719e-8412-ce5fe28dc938', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '751c0c56-40e2-4dd1-a259-e4bca5b3cf65', '019c02b5-e5cd-719e-8412-ce5fe28dc938', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '3baa0f89-afed-4237-8d66-ad7895c30501', '019c02b5-e5cd-719e-8412-ce5fe28dc938', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- UOB PRVI Miles
(gen_random_uuid(), '9873d4fb-04a0-43d6-b1aa-12b4d5117163', '019c02b5-e5cd-719e-8412-ce5fe28dc938', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- HSBC TravelOne
(gen_random_uuid(), '32c6ca39-1a72-4bb3-b814-4c13ce085cb0', '019c02b5-e5cd-719e-8412-ce5fe28dc938', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL); -- AMEX KrisFlyer

-- =====================================================
-- WinMart Deals
-- =====================================================

-- Deal 8: WinMart Cong Quynh - Weekend Discount 10%
INSERT INTO card_merchant_deals (id, card_product_id, merchant_deal_id, created_at, updated_at, deleted_at)
VALUES
-- Grocery shopping cards
(gen_random_uuid(), '0d8135c5-7187-4feb-95ca-65f443960e76', '019c02b5-e5cd-719e-8412-ce5fe28dc116', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '60cadce1-8402-4a76-8b14-6e690f89b6d1', '019c02b5-e5cd-719e-8412-ce5fe28dc116', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '75698036-81d1-493c-8557-ce034112f5cb', '019c02b5-e5cd-719e-8412-ce5fe28dc116', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- BOC Sheng Siong
(gen_random_uuid(), '10b0609e-c7b9-4cc2-bc93-004351861f7b', '019c02b5-e5cd-719e-8412-ce5fe28dc116', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '8537e696-9000-4600-a2e2-259cce6effcf', '019c02b5-e5cd-719e-8412-ce5fe28dc116', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '904d6105-f39a-4918-ab66-431217255d8e', '019c02b5-e5cd-719e-8412-ce5fe28dc116', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'fd1f1e88-fc9c-470f-a0c2-21adbb3a8cab', '019c02b5-e5cd-719e-8412-ce5fe28dc116', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- =====================================================
-- CGV Cinemas Deals
-- =====================================================

-- Deal 9: CGV Liberty Citypoint - Movie Ticket Cashback 15%
INSERT INTO card_merchant_deals (id, card_product_id, merchant_deal_id, created_at, updated_at, deleted_at)
VALUES
-- Entertainment/Lifestyle cards
(gen_random_uuid(), '751c0c56-40e2-4dd1-a259-e4bca5b3cf65', '019c02b5-e5cd-719e-8412-ce5fe28dc195', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'f6c123a3-86da-4208-addb-6b859a1c16c7', '019c02b5-e5cd-719e-8412-ce5fe28dc195', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '0d8135c5-7187-4feb-95ca-65f443960e76', '019c02b5-e5cd-719e-8412-ce5fe28dc195', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '60cadce1-8402-4a76-8b14-6e690f89b6d1', '019c02b5-e5cd-719e-8412-ce5fe28dc195', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '10b0609e-c7b9-4cc2-bc93-004351861f7b', '019c02b5-e5cd-719e-8412-ce5fe28dc195', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'ecc9198b-460a-400d-af83-582156a7071b', '019c02b5-e5cd-719e-8412-ce5fe28dc195', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- Citi M1
(gen_random_uuid(), '904d6105-f39a-4918-ab66-431217255d8e', '019c02b5-e5cd-719e-8412-ce5fe28dc195', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e77ae6a5-7e5d-4444-9ae8-593017699487', '019c02b5-e5cd-719e-8412-ce5fe28dc195', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- =====================================================
-- Watsons Deals
-- =====================================================

-- Deal 10: Watsons Bitexco - Health & Beauty Sale 25%
INSERT INTO card_merchant_deals (id, card_product_id, merchant_deal_id, created_at, updated_at, deleted_at)
VALUES
-- Beauty/Lifestyle cards
(gen_random_uuid(), '55efeb5d-87ae-4bb5-a01c-506bbd8981c3', '019c02b5-e5cd-719e-8412-ce5fe28dc632', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- UOB Lady
(gen_random_uuid(), '08935a88-d85d-4b43-99fb-6bb6a19fe736', '019c02b5-e5cd-719e-8412-ce5fe28dc632', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- UOB Lady's S
(gen_random_uuid(), 'f6c123a3-86da-4208-addb-6b859a1c16c7', '019c02b5-e5cd-719e-8412-ce5fe28dc632', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '60cadce1-8402-4a76-8b14-6e690f89b6d1', '019c02b5-e5cd-719e-8412-ce5fe28dc632', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '10b0609e-c7b9-4cc2-bc93-004351861f7b', '019c02b5-e5cd-719e-8412-ce5fe28dc632', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '904d6105-f39a-4918-ab66-431217255d8e', '019c02b5-e5cd-719e-8412-ce5fe28dc632', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), '0d8135c5-7187-4feb-95ca-65f443960e76', '019c02b5-e5cd-719e-8412-ce5fe28dc632', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);
