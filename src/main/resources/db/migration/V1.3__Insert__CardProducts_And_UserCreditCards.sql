-- =====================================================
-- Flyway Migration V1.3: Insert Card Products and User Credit Cards
-- Description: Insert 59 card products and link them to users
-- =====================================================

-- =====================================================
-- Insert Card Products (59 cards)
-- =====================================================

INSERT INTO card_products (id, card_code, card_name, card_type, image_url, created_at, updated_at, deleted_at) VALUES
-- American Express Cards (7 cards)
('d858237b-0138-4be5-bd21-0ffa7d06236b', 'AMEX_HIGHFLYER', 'Amex HighFlyer Card', 'AMERICAN_EXPRESS', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Platinum_Charge_Card_u4qo2g.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('32c6ca39-1a72-4bb3-b814-4c13ce085cb0', 'AMEX_KRIS', 'Amex Singapore Airlines KrisFlyer Credit Card', 'AMERICAN_EXPRESS', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Singapore_Airlines_KrisFlyer_Credit_Card_eoacqd.avif', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('a52ee46b-aca3-4e97-a123-eed61f0d6c4b', 'AMEX_KRIS_ASCEND', 'Amex KrisFlyer Ascend Credit Card', 'AMERICAN_EXPRESS', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_KrisFlyer_Ascend_Credit_Card_bwpes5.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('20d26838-ba7d-446a-a33c-516bf2324234', 'AMEX_PLAT_CARD', 'Amex Platinum Credit Card', 'AMERICAN_EXPRESS', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Platinum_Credit_Card_e2zfkg.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('fcc28a55-7937-4be9-a5b6-1908fb767f25', 'AMEX_PLAT_CHARGE', 'Amex Platinum Charge Card', 'AMERICAN_EXPRESS', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Platinum_Charge_Card_u4qo2g.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('2fda1fdc-364b-4af2-be34-63bed662d49a', 'AMEX_PLAT_RESERVE', 'Amex Platinum Reserve Credit Card', 'AMERICAN_EXPRESS', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Platinum_Credit_Card_e2zfkg.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('8537e696-9000-4600-a2e2-259cce6effcf', 'AMEX_TRUE', 'Amex True Cashback', 'AMERICAN_EXPRESS', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_KrisFlyer_Ascend_Credit_Card_bwpes5.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- Bank of China (BOC) Cards (3 cards)
('86809932-8ba0-4ab2-989a-1ef0eb6a835e', 'BOC_CARD_FAMILY', 'BOC Family Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/BOC_Family_Card_l6gwak.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('75698036-81d1-493c-8557-ce034112f5cb', 'BOC_CARD_SHENGSIONG', 'BOC Sheng Siong Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/BOC_Sheng_Siong_Card_obm57e.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('8fb590ec-d16b-4630-8d2b-a1d797acaa96', 'BOC_Q10_WORLD', 'BOC Qoo10 World Mastercard Credit Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/BOC_Family_Card_l6gwak.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- CIMB Cards (1 card)
('fa82c436-ff43-43df-916f-6a2a3b4437f2', 'CIMB_VISA_SIGNATURE', 'CIMB Visa Signature Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_Rewards_Card_vakprl.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- Citibank Cards (8 cards)
('60cadce1-8402-4a76-8b14-6e690f89b6d1', 'CITI_CARD_CASHBACK', 'Citi Cashback Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_Rewards_Card_vakprl.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('92255846-351a-4d69-a41e-c7d65adba916', 'CITI_CARD_CASHBACKPLUS', 'Citi Cash Back+ Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_Rewards_Card_vakprl.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('ecc9198b-460a-400d-af83-582156a7071b', 'CITI_CARD_M1', 'Citi M1 Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_M1_Card_wp4scb.avif', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('da60c099-8fb7-46c3-8182-ef077210fcb3', 'CITI_CARD_PRESTIGE', 'Citi Prestige Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_Rewards_Card_vakprl.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('1b33cc0c-3449-418e-a8cb-77660fe90d73', 'CITI_CARD_SMRT', 'Citi SMRT Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_M1_Card_wp4scb.avif', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('c58da6a8-5651-43eb-8c71-7feb459d2a54', 'CITI_PREMIER', 'Citi Premier Miles Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_Rewards_Card_vakprl.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('f6c123a3-86da-4208-addb-6b859a1c16c7', 'CITI_REWARDS', 'Citi Rewards Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_Rewards_Card_vakprl.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- DBS Cards (3 cards)
('8698c049-5b81-4601-b6ca-86063e76ac46', 'DBS_ALTITUDE_AMEX', 'DBS Altitude American Express Card', 'AMERICAN_EXPRESS', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/DBS_Altitude_Visa_Card_xxygps.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('751c0c56-40e2-4dd1-a259-e4bca5b3cf65', 'DBS_ALTITUDE_VISA', 'DBS Altitude Visa Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/DBS_Altitude_Visa_Card_xxygps.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('24b0494c-767e-4621-ad59-7cd687473b30', 'DCS_ULTIMATE_MASTER', 'DCS Ultimate Platinum Mastercard', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/DBS_Altitude_Visa_Card_xxygps.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- Grab Card (1 card)
('e77ae6a5-7e5d-4444-9ae8-593017699487', 'GRAB_CARD_GRABPAY', 'GrabPay Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/GrabPay_Card_wmxaha.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- HSBC Cards (5 cards)
('5a969d3d-4eb6-4148-929a-b19189b59196', 'HSBC_CARD_ADVANCE', 'HSBC Advance Credit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_M1_Card_wp4scb.avif', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('08b74b2e-c511-43ce-af37-3c7d150afe28', 'HSBC_CARD_LIVE_PLUS', 'HSBC Live+ Credit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/DBS_Altitude_Visa_Card_xxygps.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('9873d4fb-04a0-43d6-b1aa-12b4d5117163', 'HSBC_CARD_TRAVELONE', 'HSBC TravelOne Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Platinum_Credit_Card_e2zfkg.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('9ddca767-cf45-4ef9-94b6-bbccbd179c50', 'HSBC_CARD_VISAPLATINUM', 'HSBC Visa Platinum Credit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_Rewards_Card_vakprl.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('2e59f01f-9221-401c-a6d3-56251c25738a', 'HSBC_REVOLUTION', 'HSBC Revolution VISA', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/GrabPay_Card_wmxaha.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- Maybank Cards (5 cards)
('fd1f1e88-fc9c-470f-a0c2-21adbb3a8cab', 'MAYBANK_CARD_FAMILY', 'Maybank Family and Friends Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/BOC_Family_Card_l6gwak.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('0418caa3-fc9b-4909-8e8f-0b4f4652cc81', 'MAYBANK_CARD_VISAINFINITE', 'Maybank Visa Infinite Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Platinum_Charge_Card_u4qo2g.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('7409867a-13cc-4948-b3bc-fd89e0d438e4', 'MAYBANK_HORIZON', 'Maybank Horizon Visa Signature Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/DBS_Altitude_Visa_Card_xxygps.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('e23bc1c3-7902-4a9d-bc50-778a4db983e4', 'MAYBANK_PLATINUM', 'Maybank Platinum Visa Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_KrisFlyer_Ascend_Credit_Card_bwpes5.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('c96e2d34-c227-4ada-ba5e-21bc6941ae47', 'MAYBANK_WORLD', 'Maybank World Mastercard', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_Rewards_Card_vakprl.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- OCBC Cards (6 cards)
('0d8135c5-7187-4feb-95ca-65f443960e76', 'OCBC_CARD_365', 'OCBC 365 Credit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/BOC_Sheng_Siong_Card_obm57e.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('06347013-0cec-49be-98da-a61c8d55d52a', 'OCBC_CARD_90N_VISA', 'OCBC 90°N Visa Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/GrabPay_Card_wmxaha.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('991b15e2-f2a3-48ff-a3ec-6ff024225ff5', 'OCBC_CARD_INFINITE', 'OCBC INFINITY Cashback Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Singapore_Airlines_KrisFlyer_Credit_Card_eoacqd.avif', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('06d78c36-8b92-4b9e-978a-24b2a25ea09f', 'OCBC_DEBIT_FRANK', 'OCBC Frank Debit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_M1_Card_wp4scb.avif', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('26bb166b-3a49-454c-bfce-6206ae41bc33', 'OCBC_FRANK', 'OCBC Frank Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/DBS_Altitude_Visa_Card_xxygps.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('facb56c4-f1a5-4ae7-b015-dffafe426a0d', 'OCBC_TITANIUM', 'OCBC Rewards Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/BOC_Family_Card_l6gwak.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- POSB Cards (1 card)
('c738f7ca-9669-4cee-890b-07804eb34ae5', 'POSB_CARD_EVERYDAY', 'POSB Everyday Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Platinum_Credit_Card_e2zfkg.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- Standard Chartered (SC) Cards (7 cards)
('d858243f-a4f5-4e3b-832a-2cd0ebb54fca', 'SCB_CARD_JOURNEY', 'SC Journey Credit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_Rewards_Card_vakprl.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('8d440b8a-3eae-4ade-b59e-4eda030d4edc', 'SCB_CARD_PRUDENTIAL', 'SC Prudential Platinum Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Platinum_Charge_Card_u4qo2g.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('4cb296a4-c22f-4e69-80bb-a6d9f148b4ae', 'SCB_CARD_SMART', 'SC Smart Credit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/GrabPay_Card_wmxaha.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('904d6105-f39a-4918-ab66-431217255d8e', 'SCB_CARD_UNLIMITED', 'SC Simply Cash', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/BOC_Sheng_Siong_Card_obm57e.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('5d5230d2-02e5-4c4b-8c5b-1118135e2577', 'SCB_CARD_VI', 'SC Visa Infinite Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_KrisFlyer_Ascend_Credit_Card_bwpes5.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('0c48e176-ca64-464b-94e0-a13e39cb37ee', 'SCB_DEBIT_JUMPSTART', 'SC JumpStart Debit Card', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_M1_Card_wp4scb.avif', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('c6ec15a8-a70a-4ee1-99e1-b42abcdc7af1', 'SCB_REWARDS_PLUS', 'Standard Chartered Rewards+', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/DBS_Altitude_Visa_Card_xxygps.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- Trust Cards (5 cards)
('7ec84ce3-da88-467c-bd14-992abd1a4df2', 'TRUST_CARD_CASHBACK', 'TRUST Cashback Credit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/BOC_Family_Card_l6gwak.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('723a23a5-e01a-4743-847e-5eabbc684cda', 'TRUST_CARD_NTUC', 'NTUC Link Credit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Singapore_Airlines_KrisFlyer_Credit_Card_eoacqd.avif', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('1aad65a4-60db-4c64-b40d-336d1640ae3f', 'TRUST_CARD_TRUST', 'Trust Link Credit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_Rewards_Card_vakprl.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('40368cb2-9c49-4122-ba7b-8fcf8613c97e', 'TRUST_DEBIT_NTUC', 'NTUC Link Debit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/GrabPay_Card_wmxaha.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('27132cbd-efa1-4d13-a3ab-4e4f7bfd8ffc', 'TRUST_DEBIT_TRUST', 'Trust Link Debit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Platinum_Credit_Card_e2zfkg.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- UOB Cards (8 cards)
('a3485870-7da5-4615-a186-7c95eb83f5dd', 'UOB_CARD_ABSOLUTE', 'UOB Absolute Cashback', 'AMERICAN_EXPRESS', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Platinum_Charge_Card_u4qo2g.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('55efeb5d-87ae-4bb5-a01c-506bbd8981c3', 'UOB_CARD_LADY', 'UOB Lady', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/Citi_M1_Card_wp4scb.avif', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('10b0609e-c7b9-4cc2-bc93-004351861f7b', 'UOB_CARD_ONE', 'UOB One Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/DBS_Altitude_Visa_Card_xxygps.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('3aad2445-64b7-47e3-a7f0-5c3c77f5febe', 'UOB_CARD_PREFERRED', 'UOB Preferred Platinum', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_KrisFlyer_Ascend_Credit_Card_bwpes5.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('c7f6285a-d301-404e-95ce-0d5e366165a2', 'UOB_DEBIT_ONE', 'UOB One Debit Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/BOC_Sheng_Siong_Card_obm57e.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('08935a88-d85d-4b43-99fb-6bb6a19fe736', 'UOB_LADYS_S', 'UOB Lady', 'MASTERCARD', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581732/GrabPay_Card_wmxaha.webp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('3baa0f89-afed-4237-8d66-ad7895c30501', 'UOB_PRVI_VISA', 'UOB PRVI Miles Visa Card', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/BOC_Family_Card_l6gwak.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('6537851c-0b80-4a2b-915c-6d2880d9b30b', 'UOB_SIGNATURE', 'UOB Visa Signature', 'VISA', 'https://res.cloudinary.com/dzpv3mfjt/image/upload/v1769581731/Amex_Singapore_Airlines_KrisFlyer_Credit_Card_eoacqd.avif', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);


INSERT INTO user_credit_cards (id, user_id, card_product_id, first_payment_date, expiry_date, created_at, updated_at, deleted_at) VALUES
(gen_random_uuid(), 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', 'd858237b-0138-4be5-bd21-0ffa7d06236b', '2024-11-25', '2027-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', '60cadce1-8402-4a76-8b14-6e690f89b6d1', '2024-12-01', '2028-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', '751c0c56-40e2-4dd1-a259-e4bca5b3cf65', '2024-10-15', '2027-10-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', '10b0609e-c7b9-4cc2-bc93-004351861f7b', '2024-12-05', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', 'e77ae6a5-7e5d-4444-9ae8-593017699487', '2024-11-20', '2028-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', 'c58da6a8-5651-43eb-8c71-7feb459d2a54', '2024-12-10', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', '9873d4fb-04a0-43d6-b1aa-12b4d5117163', '2024-11-01', '2028-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', '0d8135c5-7187-4feb-95ca-65f443960e76', '2024-10-20', '2027-10-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', '86809932-8ba0-4ab2-989a-1ef0eb6a835e', '2024-12-15', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', 'c738f7ca-9669-4cee-890b-07804eb34ae5', '2024-11-10', '2028-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', '20d26838-ba7d-446a-a33c-516bf2324234', '2024-12-01', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', 'da60c099-8fb7-46c3-8182-ef077210fcb3', '2024-11-15', '2028-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', '6537851c-0b80-4a2b-915c-6d2880d9b30b', '2024-10-05', '2027-10-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', '5d5230d2-02e5-4c4b-8c5b-1118135e2577', '2024-09-20', '2027-09-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'd0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', '55efeb5d-87ae-4bb5-a01c-506bbd8981c3', '2024-12-20', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'd0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', 'fd1f1e88-fc9c-470f-a0c2-21adbb3a8cab', '2024-11-25', '2028-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', '8537e696-9000-4600-a2e2-259cce6effcf', '2024-12-25', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', 'f6c123a3-86da-4208-addb-6b859a1c16c7', '2024-11-30', '2028-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', '991b15e2-f2a3-48ff-a3ec-6ff024225ff5', '2024-10-10', '2027-10-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', '723a23a5-e01a-4743-847e-5eabbc684cda', '2024-12-22', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', '3aad2445-64b7-47e3-a7f0-5c3c77f5febe', '2025-01-05', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', '08b74b2e-c511-43ce-af37-3c7d150afe28', '2024-12-15', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'b4c5d6e7-f8a9-4b0c-1d2e-3f4a5b6c7d8e', 'a52ee46b-aca3-4e97-a123-eed61f0d6c4b', '2025-01-10', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'b4c5d6e7-f8a9-4b0c-1d2e-3f4a5b6c7d8e', '92255846-351a-4d69-a41e-c7d65adba916', '2024-12-20', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'b4c5d6e7-f8a9-4b0c-1d2e-3f4a5b6c7d8e', '7409867a-13cc-4948-b3bc-fd89e0d438e4', '2024-11-05', '2027-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', '26bb166b-3a49-454c-bfce-6206ae41bc33', '2025-01-15', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', 'd858243f-a4f5-4e3b-832a-2cd0ebb54fca', '2024-12-25', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'd6e7f8a9-b0c1-4d2e-3f4a-5b6c7d8e9f0a', '1aad65a4-60db-4c64-b40d-336d1640ae3f', '2025-01-12', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e7f8a9b0-c1d2-4e3f-4a5b-6c7d8e9f0a1b', 'fa82c436-ff43-43df-916f-6a2a3b4437f2', '2025-01-15', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e7f8a9b0-c1d2-4e3f-4a5b-6c7d8e9f0a1b', '8fb590ec-d16b-4630-8d2b-a1d797acaa96', '2024-12-28', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e7f8a9b0-c1d2-4e3f-4a5b-6c7d8e9f0a1b', 'c96e2d34-c227-4ada-ba5e-21bc6941ae47', '2024-11-12', '2027-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'f8a9b0c1-d2e3-4f4a-5b6c-7d8e9f0a1b2c', '4cb296a4-c22f-4e69-80bb-a6d9f148b4ae', '2025-01-17', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'f8a9b0c1-d2e3-4f4a-5b6c-7d8e9f0a1b2c', 'facb56c4-f1a5-4ae7-b015-dffafe426a0d', '2024-12-30', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'a9b0c1d2-e3f4-4a5b-6c7d-8e9f0a1b2c3d', '3baa0f89-afed-4237-8d66-ad7895c30501', '2025-01-20', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'a9b0c1d2-e3f4-4a5b-6c7d-8e9f0a1b2c3d', '2e59f01f-9221-401c-a6d3-56251c25738a', '2025-01-05', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'b0c1d2e3-f4a5-4b6c-7d8e-9f0a1b2c3d4e', '7ec84ce3-da88-467c-bd14-992abd1a4df2', '2025-01-22', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f', '904d6105-f39a-4918-ab66-431217255d8e', '2025-01-25', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f', 'ecc9198b-460a-400d-af83-582156a7071b', '2025-01-10', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f', 'a3485870-7da5-4615-a186-7c95eb83f5dd', '2024-12-12', '2027-12-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'd2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a', '75698036-81d1-493c-8557-ce034112f5cb', '2025-01-27', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'd2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a', '06347013-0cec-49be-98da-a61c8d55d52a', '2025-01-15', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e3f4a5b6-c7d8-4e9f-0a1b-2c3d4e5f6a7b', 'c6ec15a8-a70a-4ee1-99e1-b42abcdc7af1', '2025-01-28', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
(gen_random_uuid(), 'e3f4a5b6-c7d8-4e9f-0a1b-2c3d4e5f6a7b', '1b33cc0c-3449-418e-a8cb-77660fe90d73', '2025-01-18', '2028-01-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);