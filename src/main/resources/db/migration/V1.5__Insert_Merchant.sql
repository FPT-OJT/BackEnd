INSERT INTO merchant_categories (id, category_name, image_url)
VALUES ('019c02b0-c9aa-75f9-8af0-3dad03f9aa6e', 'Dining & Food', 'https://res.cloudinary.com/dkvga054t/image/upload/v1769569424/enjoying-a-brunch-together_kanm19.jpg'),
       ('019c02b1-00e5-7303-bcb1-8e27d6077ea7', 'Shopping & Retail', 'https://res.cloudinary.com/dkvga054t/image/upload/v1769569494/images_ygthjk.jpg'),
       ('019c02b1-00e5-7a51-8fb8-255169573de1', 'Groceries & Supermarkets', 'https://res.cloudinary.com/dkvga054t/image/upload/v1769569608/food-prices-vary-widely-among-grocery-stores-1704834105_kwuxmg.jpg'),
       ('019c02b1-00e5-7159-90fd-32645d44a8e9', 'Entertainment', 'https://res.cloudinary.com/dkvga054t/image/upload/v1769569673/18-26-1_hayxkj.jpg'),
       ('019c02b1-00e5-7996-a7fc-3cd18f88f945', 'Health & Beauty', 'https://res.cloudinary.com/dkvga054t/image/upload/v1769569721/images_s1ayc7.jpg');


INSERT INTO merchants (id, name, mcc, description, category_id, logo_url)
VALUES ('019c02b1-00e5-76a2-b661-47c415151720', 'Starbucks Vietnam', '5814', 'International coffeehouse chain', '019c02b0-c9aa-75f9-8af0-3dad03f9aa6e', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRowblgC4PDfdIlo9vW2F3Sf1n_VaOFhMIeXA&s'),
       ('019c02b1-00e5-7610-ae32-194cd6ad2ac0', 'Haidilao Hotpot', '5812', 'Famous hotpot restaurant chain', '019c02b0-c9aa-75f9-8af0-3dad03f9aa6e', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT89MVUx-5iG-UZGDMtvPB78CINDzOMJ4yvlg&s'),
       ('019c02b1-00e5-7ae5-a057-4424bf3e1413', 'Uniqlo', '5651', 'Japanese casual wear retailer', '019c02b1-00e5-7303-bcb1-8e27d6077ea7', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbUwyXs85fpEi-MxJE3k2lImkoDo25suHx0A&s'),
       ('019c02b1-00e5-7543-8a2d-0fe0b5d73c7d', 'Zara', '5691', 'Global fashion retail brand', '019c02b1-00e5-7303-bcb1-8e27d6077ea7', 'https://crystalpng.com/wp-content/uploads/2025/12/Zara-Logo.png'),
       ('019c02b1-00e5-703e-af5a-a48fdb495410', '7-Eleven', '5499', 'Convenience store chain', '019c02b1-00e5-7a51-8fb8-255169573de1', 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/7-eleven_logo.svg/330px-7-eleven_logo.svg.png'),
       ('019c02b1-00e5-7f9f-b557-25f245548613', 'WinMart', '5411', 'Vietnamese supermarket chain', '019c02b1-00e5-7a51-8fb8-255169573de1', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShKCRwbHwkqg7F_Td5nSO-D0SBjBz9EdjFog&s'),
       ('019c02b1-00e5-77f1-aa24-f427d5c6771b', 'CGV Cinemas', '7832', 'Cinema and movie entertainment', '019c02b1-00e5-7159-90fd-32645d44a8e9', 'https://gigamall.com.vn/data/2019/05/06/11365490_logo-cgv-500x500.jpg'),
       ('019c02b1-00e5-7815-81ce-97f53591e962', 'Watsons', '5977', 'Health and beauty retail chain', '019c02b1-00e5-7996-a7fc-3cd18f88f945', 'https://play-lh.googleusercontent.com/liCMCq_uGSN5LmHAUfS2m4Ki8xj_9oSrS1BvvH6-QMFf_dFQ29Eip0eP7cpEzZBa6_0'),
       ('019c02b2-1111-7000-aaaa-000000000001', 'KFC Vietnam', '5813', 'Global fast food fried chicken chain', '019c02b0-c9aa-75f9-8af0-3dad03f9aa6e', 'https://upload.wikimedia.org/wikipedia/sco/thumb/b/bf/KFC_logo.svg/512px-KFC_logo.svg.png'),
       ('019c02b2-1111-7000-aaaa-000000000002', 'Highlands Coffee', '5815', 'Popular Vietnamese coffee chain', '019c02b0-c9aa-75f9-8af0-3dad03f9aa6e', 'https://upload.wikimedia.org/wikipedia/vi/7/7c/Highlands_Coffee_logo.svg'),
       ('019c02b2-1111-7000-aaaa-000000000005', 'Co.opmart', '5412', 'Vietnamese supermarket cooperative chain', '019c02b1-00e5-7a51-8fb8-255169573de1', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSuil-KN87kWDOj7jBEXJipuRLwTDhM3Esu8Q&s'),
       ('019c02b2-1111-7000-aaaa-000000000006', 'Bach Hoa Xanh', '5331', 'Retail grocery chain in Vietnam', '019c02b1-00e5-7a51-8fb8-255169573de1', 'https://cdn.haitrieu.com/wp-content/uploads/2022/03/Logo-Bach-Hoa-Xanh-V.png'),
       ('019c02b2-1111-7000-aaaa-000000000007', 'Galaxy Cinema', '7833', 'Vietnamese cinema chain', '019c02b1-00e5-7159-90fd-32645d44a8e9', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXj6J1DZs7CADgbRBUEh_6HCdr_oJOepncNQ&s');



INSERT INTO merchant_agencies (id, name, merchant_id, longitude, latitude, image_url)
VALUES ('019c02b1-00e5-771c-ab2a-a29038f14e43', 'Starbucks New World', '019c02b1-00e5-76a2-b661-47c415151720', 106.694419, 10.771918, NULL),
       ('019c02b1-00e5-71df-b21c-15fd36d72cb0', 'Starbucks Rex Hotel', '019c02b1-00e5-76a2-b661-47c415151720', 106.701944, 10.776111, NULL),
       ('019c02b5-e5cd-76a1-a5bd-3aa7b05d6cdd', 'Starbucks Landmark 81', '019c02b1-00e5-76a2-b661-47c415151720', 106.721944, 10.795111, NULL),
       ('019c02b5-e5cd-7f99-af6e-a7be68d12e09', 'Haidilao Vincom Center', '019c02b1-00e5-7610-ae32-194cd6ad2ac0', 106.702222, 10.778056, NULL),
       ('019c02b5-e5cd-7ed5-817e-ece3461c225a', 'Haidilao Bitexco', '019c02b1-00e5-7610-ae32-194cd6ad2ac0', 106.704500, 10.771500, NULL),
       ('019c02b5-e5cd-7270-963a-cfd6461c4c3d', 'Uniqlo Dong Khoi', '019c02b1-00e5-7ae5-a057-4424bf3e1413', 106.702500, 10.778333, NULL),
       ('019c02b5-e5cd-7e3c-8e8d-fe7a6ca2177e', 'Uniqlo Saigon Centre', '019c02b1-00e5-7ae5-a057-4424bf3e1413', 106.703611, 10.773889, NULL),
       ('019c02b5-e5cd-70bc-bc1b-fe63e5096866', 'Zara Vincom Center', '019c02b1-00e5-7543-8a2d-0fe0b5d73c7d', 106.702200, 10.778000, NULL),
       ('019c02b5-e5cd-79a8-9a05-38996603d280', '7-Eleven Saigon Trade Center', '019c02b1-00e5-703e-af5a-a48fdb495410', 106.705278, 10.783889, NULL),
       ('019c02b5-e5cd-7b71-bae9-f03440d533fb', '7-Eleven Millennium', '019c02b1-00e5-703e-af5a-a48fdb495410', 106.698333, 10.763333, NULL),
       ('019c02b5-e5cd-7b46-a00a-4c78dab90823', '7-Eleven Sala', '019c02b1-00e5-703e-af5a-a48fdb495410', 106.719444, 10.770556, NULL),
       ('019c02b5-e5cd-7afb-a7c0-3a839ba37ac4', 'WinMart Cong Quynh', '019c02b1-00e5-7f9f-b557-25f245548613', 106.689444, 10.766389, NULL),
       ('019c02b5-e5cd-7c77-a341-19e0794d5205', 'CGV Liberty Citypoint', '019c02b1-00e5-77f1-aa24-f427d5c6771b', 106.701389, 10.774444, NULL),
       ('019c02b5-e5cd-7f03-a877-fd848930f6e5', 'CGV Vivo City', '019c02b1-00e5-77f1-aa24-f427d5c6771b', 106.700833, 10.730833, NULL),
       ('019c02b5-e5cd-7106-bc93-b35b7357bc80', 'Watsons Bitexco', '019c02b1-00e5-7815-81ce-97f53591e962', 106.704500, 10.771500, NULL),
       ('019c02b5-e5cd-7858-8130-c2a8503cb25d', 'Watsons Aeon Tan Phu', '019c02b1-00e5-7815-81ce-97f53591e962', 106.615278, 10.801667, NULL);


INSERT INTO merchant_deals (id, merchant_agency_id, deal_name, discount_rate, cashback_rate, points_multiplier, description, valid_from, valid_to)
VALUES ('019c02b5-e5cd-719e-8412-ce5fe28dc468', '019c02b1-00e5-771c-ab2a-a29038f14e43', 'Buy 1 Get 1 Coffee', 50.0, , 1.5, 'Buy one coffee and get another free for the same or lower price.', '2026-02-01', '2026-02-28'),
       ('019c02b5-e5cd-719e-8412-ce5fe28dc479', '019c02b1-00e5-71df-b21c-15fd36d72cb0', 'Morning Coffee Discount', 20.0, , 1.2, '20% off for all drinks before 10 AM.', '2026-02-01', '2026-03-31'),
       ('019c02b5-e5cd-719e-8412-ce5fe28dc490', '019c02b5-e5cd-7f99-af6e-a7be68d12e09', 'Hotpot Cashback', , 10.0, 2.0, 'Get 10% cashback when dining in.', '2026-01-15', '2026-04-15'),
       ('019c02b5-e5cd-719e-8412-ce5fe28dc781', '019c02b5-e5cd-7270-963a-cfd6461c4c3d', 'Season Sale', 30.0, , 1.0, 'Up to 30% off selected seasonal items.', '2026-02-10', '2026-03-10'),
       ('019c02b5-e5cd-719e-8412-ce5fe28dc112', '019c02b5-e5cd-70bc-bc1b-fe63e5096866', 'Member Exclusive Discount', 15.0, , 1.3, 'Exclusive discount for loyalty members.', '2026-02-01', '2026-02-20'),
       ('019c02b5-e5cd-719e-8412-ce5fe28dc128', '019c02b5-e5cd-79a8-9a05-38996603d280', 'Daily Cashback', , 5.0, 1.0, '5% cashback for all purchases.', '2026-01-01', '2026-12-31'),
       ('019c02b5-e5cd-719e-8412-ce5fe28dc938', '019c02b5-e5cd-7b71-bae9-f03440d533fb', 'Points Booster', , , 2.0, 'Earn double reward points for every purchase.', '2026-02-01', '2026-06-30'),
       ('019c02b5-e5cd-719e-8412-ce5fe28dc116', '019c02b5-e5cd-7afb-a7c0-3a839ba37ac4', 'Weekend Discount', 10.0, , 1.1, '10% off for shopping on weekends.', '2026-02-01', '2026-05-31'),
       ('019c02b5-e5cd-719e-8412-ce5fe28dc195', '019c02b5-e5cd-7c77-a341-19e0794d5205', 'Movie Ticket Cashback', , 15.0, 1.5, '15% cashback on movie tickets.', '2026-02-01', '2026-04-30'),
       ('019c02b5-e5cd-719e-8412-ce5fe28dc632', '019c02b5-e5cd-7106-bc93-b35b7357bc80', 'Health & Beauty Sale', 25.0, , 1.2, 'Save 25% on selected health and beauty products.', '2026-02-05', '2026-03-05');
