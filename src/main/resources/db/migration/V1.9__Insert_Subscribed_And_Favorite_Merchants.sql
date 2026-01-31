-- =====================================================
-- Flyway Migration V1.9: Insert Subscribed and Favorite Merchants Data
-- Description: Realistic data based on user personality analysis
-- =====================================================

-- User Personality Profiles:
-- 1. John Smith (john_smith) - Introvert, home delivery enthusiast, late-night coder
-- 2. Sarah Johnson (sarah_johnson) - Fashion lover, social butterfly, coffee addict
-- 3. Michael Brown (michael_brown) - Fitness enthusiast, healthy eating, early bird
-- 4. Emily Davis (emily_davis) - Movie buff, entertainment seeker, weekend warrior
-- 5. David Wilson (david_wilson) - Busy professional, convenience store regular, quick meals
-- 6. Jessica Moore (jessica_moore) - Beauty & wellness focused, self-care advocate
-- 7. James Taylor (james_taylor) - Foodie, hotpot lover, fine dining enthusiast
-- 8. Ashley Anderson (ashley_anderson) - Budget-conscious shopper, supermarket loyalty
-- 9. Robert Thomas (robert_thomas) - Coffee connoisseur, remote worker, cafe hopper
-- 10. Amanda Jackson (amanda_jackson) - Social gatherings, restaurants, group activities
-- 11. Christopher White (christopher_white) - Tech-savvy, efficient shopper, prefers convenience
-- 12. Melissa Harris (melissa_harris) - Family oriented, grocery shopping, bulk buyer
-- 13. Daniel Martin (daniel_martin) - Young professional, fashion forward, trendy
-- 14. Jennifer Garcia (jennifer_garcia) - Health conscious, organic products, wellness
-- 15. Matthew Martinez (matthew_martinez) - Entertainment seeker, cinema regular, pop culture fan
-- 16. Stephanie Robinson (stephanie_robinson) - Practical shopper, deals hunter, savings oriented
-- 17. Anthony Clark (anthony_clark) - Fast food lover, quick service, on-the-go
-- 18. Nicole Rodriguez (nicole_rodriguez) - Skincare enthusiast, beauty products collector
-- 19. William Lewis (william_lewis) - Traditional shopper, prefers established brands

-- =====================================================
-- SUBSCRIBED MERCHANTS (Users want notifications about deals)
-- =====================================================

INSERT INTO subscribed_merchants (id, user_id, merchant_agency_id, created_at, updated_at, deleted_at) VALUES
-- John Smith (Introvert, home delivery) - Subscribes to fast food and coffee for delivery
('019d0001-0001-0001-0001-000000000001', 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', '019c02b1-00e5-771c-ab2a-a29038f14e43', '2025-11-25 10:30:00', '2025-11-25 10:30:00', NULL), -- Starbucks New World
('019d0001-0001-0001-0001-000000000002', 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', '019c02b5-e5cd-76a1-a5bd-3aa7b05d6cdd', '2025-11-26 14:20:00', '2025-11-26 14:20:00', NULL), -- Starbucks Landmark 81
('019d0001-0001-0001-0001-000000000003', 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', '019c02b5-e5cd-7b71-bae9-f03440d533fb', '2025-12-02 09:15:00', '2025-12-02 09:15:00', NULL), -- 7-Eleven Millennium (late-night snacks)

-- Sarah Johnson (Fashion & coffee lover) - Subscribes to fashion retailers and coffee shops
('019d0001-0001-0002-0002-000000000001', 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', '019c02b5-e5cd-7270-963a-cfd6461c4c3d', '2025-12-03 11:00:00', '2025-12-03 11:00:00', NULL), -- Uniqlo Dong Khoi
('019d0001-0001-0002-0002-000000000002', 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', '019c02b5-e5cd-7e3c-8e8d-fe7a6ca2177e', '2025-12-03 11:05:00', '2025-12-03 11:05:00', NULL), -- Uniqlo Saigon Centre
('019d0001-0001-0002-0002-000000000003', 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', '019c02b5-e5cd-70bc-bc1b-fe63e5096866', '2025-12-03 11:10:00', '2025-12-03 11:10:00', NULL), -- Zara Vincom Center
('019d0001-0001-0002-0002-000000000004', 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', '019c02b1-00e5-71df-b21c-15fd36d72cb0', '2025-12-05 08:30:00', '2025-12-05 08:30:00', NULL), -- Starbucks Rex Hotel
('019d0001-0001-0002-0002-000000000005', 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', '019c02b6-aa02-0000-0000-000000000002', '2025-12-08 09:00:00', '2025-12-08 09:00:00', NULL), -- Highlands Coffee

-- Michael Brown (Fitness & healthy eating) - Subscribes to supermarkets and health stores
('019d0001-0001-0003-0003-000000000001', 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', '019c02b5-e5cd-7afb-a7c0-3a839ba37ac4', '2025-12-07 07:00:00', '2025-12-07 07:00:00', NULL), -- WinMart Cong Quynh
('019d0001-0001-0003-0003-000000000002', 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', '019c02b6-aa04-0000-0000-000000000006', '2025-12-07 07:15:00', '2025-12-07 07:15:00', NULL), -- Bach Hoa Xanh
('019d0001-0001-0003-0003-000000000003', 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', '019c02b6-aa03-0000-0000-000000000005', '2025-12-10 06:45:00', '2025-12-10 06:45:00', NULL), -- Co.opmart
('019d0001-0001-0003-0003-000000000004', 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', '019c02b5-e5cd-7106-bc93-b35b7357bc80', '2025-12-12 15:30:00', '2025-12-12 15:30:00', NULL), -- Watsons Bitexco (health supplements)

-- Emily Davis (Movie buff) - Subscribes to cinemas
('019d0001-0001-0004-0004-000000000001', 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', '019c02b5-e5cd-7c77-a341-19e0794d5205', '2025-12-12 16:00:00', '2025-12-12 16:00:00', NULL), -- CGV Liberty Citypoint
('019d0001-0001-0004-0004-000000000002', 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', '019c02b5-e5cd-7f03-a877-fd848930f6e5', '2025-12-12 16:05:00', '2025-12-12 16:05:00', NULL), -- CGV Vivo City
('019d0001-0001-0004-0004-000000000003', 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', '019c02b6-aa05-0000-0000-000000000007', '2025-12-12 16:10:00', '2025-12-12 16:10:00', NULL), -- Galaxy Cinema

-- David Wilson (Busy professional, convenience stores) - Subscribes to 7-Eleven locations
('019d0001-0001-0005-0005-000000000001', 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', '019c02b5-e5cd-79a8-9a05-38996603d280', '2025-12-16 08:00:00', '2025-12-16 08:00:00', NULL), -- 7-Eleven Saigon Trade Center
('019d0001-0001-0005-0005-000000000002', 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', '019c02b5-e5cd-7b71-bae9-f03440d533fb', '2025-12-16 08:05:00', '2025-12-16 08:05:00', NULL), -- 7-Eleven Millennium
('019d0001-0001-0005-0005-000000000003', 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', '019c02b5-e5cd-7b46-a00a-4c78dab90823', '2025-12-16 08:10:00', '2025-12-16 08:10:00', NULL), -- 7-Eleven Sala
('019d0001-0001-0005-0005-000000000004', 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', '019c02b6-aa01-0000-0000-000000000001', '2025-12-18 12:30:00', '2025-12-18 12:30:00', NULL), -- KFC Vietnam (quick lunch)

-- Jessica Moore (Beauty & wellness) - Subscribes to Watsons locations
('019d0001-0001-0006-0006-000000000001', 'd0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', '019c02b5-e5cd-7106-bc93-b35b7357bc80', '2025-12-19 14:00:00', '2025-12-19 14:00:00', NULL), -- Watsons Bitexco
('019d0001-0001-0006-0006-000000000002', 'd0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', '019c02b5-e5cd-7858-8130-c2a8503cb25d', '2025-12-19 14:05:00', '2025-12-19 14:05:00', NULL), -- Watsons Aeon Tan Phu

-- James Taylor (Foodie, hotpot lover) - Subscribes to fine dining
('019d0001-0001-0007-0007-000000000001', 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', '019c02b5-e5cd-7f99-af6e-a7be68d12e09', '2025-12-21 18:00:00', '2025-12-21 18:00:00', NULL), -- Haidilao Vincom Center
('019d0001-0001-0007-0007-000000000002', 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', '019c02b5-e5cd-7ed5-817e-ece3461c225a', '2025-12-21 18:05:00', '2025-12-21 18:05:00', NULL), -- Haidilao Bitexco

-- Ashley Anderson (Budget shopper) - Subscribes to all supermarkets for deals
('019d0001-0001-0008-0008-000000000001', 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', '019c02b5-e5cd-7afb-a7c0-3a839ba37ac4', '2025-12-23 10:00:00', '2025-12-23 10:00:00', NULL), -- WinMart Cong Quynh
('019d0001-0001-0008-0008-000000000002', 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', '019c02b6-aa03-0000-0000-000000000005', '2025-12-23 10:05:00', '2025-12-23 10:05:00', NULL), -- Co.opmart
('019d0001-0001-0008-0008-000000000003', 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', '019c02b6-aa04-0000-0000-000000000006', '2025-12-23 10:10:00', '2025-12-23 10:10:00', NULL), -- Bach Hoa Xanh

-- Robert Thomas (Coffee connoisseur, remote worker) - Subscribes to all coffee shops
('019d0001-0001-0009-0009-000000000001', 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', '019c02b1-00e5-771c-ab2a-a29038f14e43', '2026-01-03 09:00:00', '2026-01-03 09:00:00', NULL), -- Starbucks New World
('019d0001-0001-0009-0009-000000000002', 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', '019c02b1-00e5-71df-b21c-15fd36d72cb0', '2026-01-03 09:05:00', '2026-01-03 09:05:00', NULL), -- Starbucks Rex Hotel
('019d0001-0001-0009-0009-000000000003', 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', '019c02b5-e5cd-76a1-a5bd-3aa7b05d6cdd', '2026-01-03 09:10:00', '2026-01-03 09:10:00', NULL), -- Starbucks Landmark 81
('019d0001-0001-0009-0009-000000000004', 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', '019c02b6-aa02-0000-0000-000000000002', '2026-01-03 09:15:00', '2026-01-03 09:15:00', NULL), -- Highlands Coffee

-- Matthew Martinez (Entertainment seeker, cinema regular) - Subscribes to all cinemas
('019d0001-0001-0015-0015-000000000001', 'a9b0c1d2-e3f4-4a5b-6c7d-8e9f0a1b2c3d', '019c02b5-e5cd-7c77-a341-19e0794d5205', '2026-01-17 15:00:00', '2026-01-17 15:00:00', NULL), -- CGV Liberty Citypoint
('019d0001-0001-0015-0015-000000000002', 'a9b0c1d2-e3f4-4a5b-6c7d-8e9f0a1b2c3d', '019c02b5-e5cd-7f03-a877-fd848930f6e5', '2026-01-17 15:05:00', '2026-01-17 15:05:00', NULL), -- CGV Vivo City
('019d0001-0001-0015-0015-000000000003', 'a9b0c1d2-e3f4-4a5b-6c7d-8e9f0a1b2c3d', '019c02b6-aa05-0000-0000-000000000007', '2026-01-17 15:10:00', '2026-01-17 15:10:00', NULL), -- Galaxy Cinema

-- Nicole Rodriguez (Skincare enthusiast) - Subscribes to beauty stores
('019d0001-0001-0018-0018-000000000001', 'd2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a', '019c02b5-e5cd-7106-bc93-b35b7357bc80', '2026-01-23 13:00:00', '2026-01-23 13:00:00', NULL), -- Watsons Bitexco
('019d0001-0001-0018-0018-000000000002', 'd2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a', '019c02b5-e5cd-7858-8130-c2a8503cb25d', '2026-01-23 13:05:00', '2026-01-23 13:05:00', NULL); -- Watsons Aeon Tan Phu


-- =====================================================
-- FAVORITE MERCHANTS (Quick access for frequent visits)
-- =====================================================

INSERT INTO favorite_merchants (id, user_id, merchant_agency_id, created_at, updated_at, deleted_at) VALUES
-- John Smith (Introvert, home delivery) - Favorites his go-to delivery spots
('019d0002-0001-0001-0001-000000000001', 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', '019c02b1-00e5-771c-ab2a-a29038f14e43', '2025-11-28 20:00:00', '2025-11-28 20:00:00', NULL), -- Starbucks New World (late-night coffee)
('019d0002-0001-0001-0001-000000000002', 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', '019c02b6-aa01-0000-0000-000000000001', '2025-12-01 22:30:00', '2025-12-01 22:30:00', NULL), -- KFC Vietnam (comfort food)
('019d0002-0001-0001-0001-000000000003', 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', '019c02b5-e5cd-7b71-bae9-f03440d533fb', '2025-12-05 23:45:00', '2025-12-05 23:45:00', NULL), -- 7-Eleven Millennium (midnight snacks)

-- Sarah Johnson (Fashion & coffee lover) - Favorites shopping destinations
('019d0002-0001-0002-0002-000000000001', 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', '019c02b5-e5cd-70bc-bc1b-fe63e5096866', '2025-12-06 14:00:00', '2025-12-06 14:00:00', NULL), -- Zara Vincom Center
('019d0002-0001-0002-0002-000000000002', 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', '019c02b1-00e5-71df-b21c-15fd36d72cb0', '2025-12-06 14:30:00', '2025-12-06 14:30:00', NULL), -- Starbucks Rex Hotel (shopping break)
('019d0002-0001-0002-0002-000000000003', 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', '019c02b5-e5cd-7e3c-8e8d-fe7a6ca2177e', '2025-12-10 16:00:00', '2025-12-10 16:00:00', NULL), -- Uniqlo Saigon Centre

-- Michael Brown (Fitness & healthy eating) - Favorites health-focused stores
('019d0002-0001-0003-0003-000000000001', 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', '019c02b6-aa04-0000-0000-000000000006', '2025-12-11 06:30:00', '2025-12-11 06:30:00', NULL), -- Bach Hoa Xanh (fresh produce)
('019d0002-0001-0003-0003-000000000002', 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', '019c02b5-e5cd-7106-bc93-b35b7357bc80', '2025-12-15 07:00:00', '2025-12-15 07:00:00', NULL), -- Watsons Bitexco (supplements)

-- Emily Davis (Movie buff) - Favorites her cinema
('019d0002-0001-0004-0004-000000000001', 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', '019c02b5-e5cd-7c77-a341-19e0794d5205', '2025-12-14 19:00:00', '2025-12-14 19:00:00', NULL), -- CGV Liberty Citypoint (weekend movies)
('019d0002-0001-0004-0004-000000000002', 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', '019c02b6-aa05-0000-0000-000000000007', '2025-12-20 20:00:00', '2025-12-20 20:00:00', NULL), -- Galaxy Cinema

-- David Wilson (Busy professional) - Favorites convenience locations near work/home
('019d0002-0001-0005-0005-000000000001', 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', '019c02b5-e5cd-79a8-9a05-38996603d280', '2025-12-17 07:30:00', '2025-12-17 07:30:00', NULL), -- 7-Eleven Saigon Trade Center (near office)
('019d0002-0001-0005-0005-000000000002', 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', '019c02b5-e5cd-7b71-bae9-f03440d533fb', '2025-12-17 19:00:00', '2025-12-17 19:00:00', NULL), -- 7-Eleven Millennium (near home)
('019d0002-0001-0005-0005-000000000003', 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', '019c02b6-aa01-0000-0000-000000000001', '2025-12-19 12:00:00', '2025-12-19 12:00:00', NULL), -- KFC Vietnam (quick lunch)

-- Jessica Moore (Beauty & wellness) - Favorites beauty stores
('019d0002-0001-0006-0006-000000000001', 'd0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', '019c02b5-e5cd-7106-bc93-b35b7357bc80', '2025-12-20 15:00:00', '2025-12-20 15:00:00', NULL), -- Watsons Bitexco (main shopping location)
('019d0002-0001-0006-0006-000000000002', 'd0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', '019c02b5-e5cd-7858-8130-c2a8503cb25d', '2025-12-22 16:00:00', '2025-12-22 16:00:00', NULL), -- Watsons Aeon Tan Phu

-- James Taylor (Foodie, hotpot lover) - Favorites fine dining spots
('019d0002-0001-0007-0007-000000000001', 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', '019c02b5-e5cd-7f99-af6e-a7be68d12e09', '2025-12-23 19:00:00', '2025-12-23 19:00:00', NULL), -- Haidilao Vincom Center (favorite hotpot)
('019d0002-0001-0007-0007-000000000002', 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', '019c02b5-e5cd-7ed5-817e-ece3461c225a', '2025-12-24 18:30:00', '2025-12-24 18:30:00', NULL), -- Haidilao Bitexco
('019d0002-0001-0007-0007-000000000003', 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', '019c02b6-aa02-0000-0000-000000000002', '2025-12-25 10:00:00', '2025-12-25 10:00:00', NULL), -- Highlands Coffee (post-meal coffee)

-- Ashley Anderson (Budget shopper) - Favorites best deal supermarkets
('019d0002-0001-0008-0008-000000000001', 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', '019c02b6-aa04-0000-0000-000000000006', '2025-12-24 09:00:00', '2025-12-24 09:00:00', NULL), -- Bach Hoa Xanh (best prices)
('019d0002-0001-0008-0008-000000000002', 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', '019c02b6-aa03-0000-0000-000000000005', '2025-12-25 10:00:00', '2025-12-25 10:00:00', NULL), -- Co.opmart

-- Robert Thomas (Coffee connoisseur, remote worker) - Favorites his workspace cafes
('019d0002-0001-0009-0009-000000000001', 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', '019c02b1-00e5-771c-ab2a-a29038f14e43', '2026-01-04 08:00:00', '2026-01-04 08:00:00', NULL), -- Starbucks New World (morning workspace)
('019d0002-0001-0009-0009-000000000002', 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', '019c02b6-aa02-0000-0000-000000000002', '2026-01-04 14:00:00', '2026-01-04 14:00:00', NULL), -- Highlands Coffee (afternoon workspace)
('019d0002-0001-0009-0009-000000000003', 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', '019c02b5-e5cd-76a1-a5bd-3aa7b05d6cdd', '2026-01-05 16:00:00', '2026-01-05 16:00:00', NULL), -- Starbucks Landmark 81 (premium workspace)

-- Amanda Jackson (Social gatherings) - Favorites group dining spots
('019d0002-0001-0010-0010-000000000001', 'b4c5d6e7-f8a9-4b0c-1d2e-3f4a5b6c7d8e', '019c02b5-e5cd-7f99-af6e-a7be68d12e09', '2026-01-06 18:00:00', '2026-01-06 18:00:00', NULL), -- Haidilao Vincom Center (group dining)
('019d0002-0001-0010-0010-000000000002', 'b4c5d6e7-f8a9-4b0c-1d2e-3f4a5b6c7d8e', '019c02b5-e5cd-7c77-a341-19e0794d5205', '2026-01-07 20:00:00', '2026-01-07 20:00:00', NULL), -- CGV Liberty Citypoint (group movies)

-- Christopher White (Tech-savvy, efficient) - Favorites convenient locations
('019d0002-0001-0011-0011-000000000001', 'c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', '019c02b5-e5cd-79a8-9a05-38996603d280', '2026-01-09 08:30:00', '2026-01-09 08:30:00', NULL), -- 7-Eleven Saigon Trade Center
('019d0002-0001-0011-0011-000000000002', 'c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', '019c02b5-e5cd-7b46-a00a-4c78dab90823', '2026-01-10 09:00:00', '2026-01-10 09:00:00', NULL), -- 7-Eleven Sala

-- Melissa Harris (Family oriented, bulk buyer) - Favorites family shopping centers
('019d0002-0001-0012-0012-000000000001', 'd6e7f8a9-b0c1-4d2e-3f4a-5b6c7d8e9f0a', '019c02b6-aa03-0000-0000-000000000005', '2026-01-11 10:00:00', '2026-01-11 10:00:00', NULL), -- Co.opmart (family shopping)
('019d0002-0001-0012-0012-000000000002', 'd6e7f8a9-b0c1-4d2e-3f4a-5b6c7d8e9f0a', '019c02b5-e5cd-7afb-a7c0-3a839ba37ac4', '2026-01-12 11:00:00', '2026-01-12 11:00:00', NULL), -- WinMart Cong Quynh

-- Daniel Martin (Young professional, fashion forward) - Favorites trendy fashion stores
('019d0002-0001-0013-0013-000000000001', 'e7f8a9b0-c1d2-4e3f-4a5b-6c7d8e9f0a1b', '019c02b5-e5cd-70bc-bc1b-fe63e5096866', '2026-01-13 15:00:00', '2026-01-13 15:00:00', NULL), -- Zara Vincom Center
('019d0002-0001-0013-0013-000000000002', 'e7f8a9b0-c1d2-4e3f-4a5b-6c7d8e9f0a1b', '019c02b5-e5cd-7e3c-8e8d-fe7a6ca2177e', '2026-01-14 16:00:00', '2026-01-14 16:00:00', NULL), -- Uniqlo Saigon Centre
('019d0002-0001-0013-0013-000000000003', 'e7f8a9b0-c1d2-4e3f-4a5b-6c7d8e9f0a1b', '019c02b1-00e5-71df-b21c-15fd36d72cb0', '2026-01-15 11:00:00', '2026-01-15 11:00:00', NULL), -- Starbucks Rex Hotel

-- Jennifer Garcia (Health conscious, organic) - Favorites health stores
('019d0002-0001-0014-0014-000000000001', 'f8a9b0c1-d2e3-4f4a-5b6c-7d8e9f0a1b2c', '019c02b6-aa04-0000-0000-000000000006', '2026-01-15 08:00:00', '2026-01-15 08:00:00', NULL), -- Bach Hoa Xanh (organic section)
('019d0002-0001-0014-0014-000000000002', 'f8a9b0c1-d2e3-4f4a-5b6c-7d8e9f0a1b2c', '019c02b5-e5cd-7858-8130-c2a8503cb25d', '2026-01-16 14:00:00', '2026-01-16 14:00:00', NULL), -- Watsons Aeon Tan Phu

-- Matthew Martinez (Entertainment seeker) - Favorites entertainment venues
('019d0002-0001-0015-0015-000000000001', 'a9b0c1d2-e3f4-4a5b-6c7d-8e9f0a1b2c3d', '019c02b6-aa05-0000-0000-000000000007', '2026-01-18 19:00:00', '2026-01-18 19:00:00', NULL), -- Galaxy Cinema (favorite cinema)
('019d0002-0001-0015-0015-000000000002', 'a9b0c1d2-e3f4-4a5b-6c7d-8e9f0a1b2c3d', '019c02b5-e5cd-7c77-a341-19e0794d5205', '2026-01-19 20:00:00', '2026-01-19 20:00:00', NULL), -- CGV Liberty Citypoint

-- Stephanie Robinson (Practical, deals hunter) - Favorites discount locations
('019d0002-0001-0016-0016-000000000001', 'b0c1d2e3-f4a5-4b6c-7d8e-9f0a1b2c3d4e', '019c02b6-aa03-0000-0000-000000000005', '2026-01-19 09:00:00', '2026-01-19 09:00:00', NULL), -- Co.opmart
('019d0002-0001-0016-0016-000000000002', 'b0c1d2e3-f4a5-4b6c-7d8e-9f0a1b2c3d4e', '019c02b5-e5cd-7afb-a7c0-3a839ba37ac4', '2026-01-20 10:00:00', '2026-01-20 10:00:00', NULL), -- WinMart Cong Quynh

-- Anthony Clark (Fast food lover) - Favorites quick service restaurants
('019d0002-0001-0017-0017-000000000001', 'c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f', '019c02b6-aa01-0000-0000-000000000001', '2026-01-21 12:00:00', '2026-01-21 12:00:00', NULL), -- KFC Vietnam
('019d0002-0001-0017-0017-000000000002', 'c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f', '019c02b5-e5cd-79a8-9a05-38996603d280', '2026-01-21 19:00:00', '2026-01-21 19:00:00', NULL), -- 7-Eleven Saigon Trade Center

-- Nicole Rodriguez (Skincare enthusiast) - Favorites beauty destinations
('019d0002-0001-0018-0018-000000000001', 'd2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a', '019c02b5-e5cd-7106-bc93-b35b7357bc80', '2026-01-24 14:00:00', '2026-01-24 14:00:00', NULL), -- Watsons Bitexco (main beauty shopping)
('019d0002-0001-0018-0018-000000000002', 'd2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a', '019c02b5-e5cd-7858-8130-c2a8503cb25d', '2026-01-25 15:00:00', '2026-01-25 15:00:00', NULL), -- Watsons Aeon Tan Phu

-- William Lewis (Traditional shopper) - Favorites established stores
('019d0002-0001-0019-0019-000000000001', 'e3f4a5b6-c7d8-4e9f-0a1b-2c3d4e5f6a7b', '019c02b1-00e5-771c-ab2a-a29038f14e43', '2026-01-25 10:00:00', '2026-01-25 10:00:00', NULL), -- Starbucks New World
('019d0002-0001-0019-0019-000000000002', 'e3f4a5b6-c7d8-4e9f-0a1b-2c3d4e5f6a7b', '019c02b6-aa03-0000-0000-000000000005', '2026-01-26 09:00:00', '2026-01-26 09:00:00', NULL); -- Co.opmart
