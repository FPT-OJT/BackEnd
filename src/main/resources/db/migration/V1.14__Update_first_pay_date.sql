ALTER TABLE user_credit_cards
ADD COLUMN first_payment_date_tmp INT;

UPDATE user_credit_cards
SET first_payment_date_tmp = EXTRACT(DAY FROM first_payment_date);

ALTER TABLE user_credit_cards
DROP COLUMN first_payment_date;

ALTER TABLE user_credit_cards
RENAME COLUMN first_payment_date_tmp TO first_payment_date;
