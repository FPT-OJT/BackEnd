-- =====================================================
-- Flyway Migration V1.1: Initial Schema
-- Description: Create all base tables for the application
-- =====================================================


-- User Management Tables
-- =====================================================

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_name VARCHAR(255) UNIQUE,
    google_id VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    role VARCHAR(50) NOT NULL,
    password VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Create index for user queries
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_name ON users(user_name);
CREATE INDEX idx_users_google_id ON users(google_id);

-- =====================================================
-- Card Product Tables
-- =====================================================

-- Card products table
CREATE TABLE card_products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    card_name VARCHAR(255) NOT NULL,
    card_type VARCHAR(100) NOT NULL,
    image_url VARCHAR(500),
    card_code VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Create index for card product queries
CREATE INDEX idx_card_products_card_code ON card_products(card_code);
CREATE INDEX idx_card_products_card_type ON card_products(card_type);

-- User credit cards (n:n relation between users and card_products)
CREATE TABLE user_credit_cards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    card_product_id UUID NOT NULL,
    first_payment_date DATE,
    expiry_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_user_credit_cards_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_credit_cards_card_product FOREIGN KEY (card_product_id) REFERENCES card_products(id) ON DELETE CASCADE
);

-- Create indexes for user credit card queries
CREATE INDEX idx_user_credit_cards_user_id ON user_credit_cards(user_id);
CREATE INDEX idx_user_credit_cards_card_product_id ON user_credit_cards(card_product_id);
CREATE INDEX idx_user_credit_cards_expiry_date ON user_credit_cards(expiry_date);

-- Card rules table
CREATE TABLE card_rules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    card_product_id UUID NOT NULL,
    match_conditions JSONB,
    match_allow_mccs JSONB,
    match_reject_mccs JSONB,
    effect_cashback_rate DOUBLE PRECISION,
    effect_points_rate DOUBLE PRECISION,
    effect_rebate_rate DOUBLE PRECISION,
    effect_merchant_discount_rate DOUBLE PRECISION,
    effect_fee_rate DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_card_rules_card_product FOREIGN KEY (card_product_id) REFERENCES card_products(id) ON DELETE CASCADE
);

-- Create index for card rules queries
CREATE INDEX idx_card_rules_card_product_id ON card_rules(card_product_id);

-- Create GIN indexes for JSONB fields (optimize JSON queries)
CREATE INDEX idx_card_rules_match_conditions ON card_rules USING GIN (match_conditions);
CREATE INDEX idx_card_rules_allow_mccs ON card_rules USING GIN (match_allow_mccs);
CREATE INDEX idx_card_rules_reject_mccs ON card_rules USING GIN (match_reject_mccs);

-- =====================================================
-- Condition Tables
-- =====================================================

-- Conditions table
CREATE TABLE conditions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(100),
    label VARCHAR(255),
    condition_code VARCHAR(100) UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Create index for condition queries
CREATE INDEX idx_conditions_condition_code ON conditions(condition_code);

-- Condition options table
CREATE TABLE condition_options (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    condition_id UUID NOT NULL,
    value VARCHAR(255),
    label VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_condition_options_condition FOREIGN KEY (condition_id) REFERENCES conditions(id) ON DELETE CASCADE
);

-- Create index for condition options queries
CREATE INDEX idx_condition_options_condition_id ON condition_options(condition_id);

-- =====================================================
-- Merchant Tables
-- =====================================================

-- Merchant categories table
CREATE TABLE merchant_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_name VARCHAR(255),
    image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Merchants table
CREATE TABLE merchants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    mcc VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(1000),
    category_id UUID NOT NULL,
    logo_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_merchants_category FOREIGN KEY (category_id) REFERENCES merchant_categories(id) ON DELETE RESTRICT
);

-- Create index for merchant queries
CREATE INDEX idx_merchants_category_id ON merchants(category_id);
CREATE INDEX idx_merchants_name ON merchants(name);
CREATE INDEX idx_merchants_mcc ON merchants(mcc);

-- Merchant agencies table
CREATE TABLE merchant_agencies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    merchant_id UUID NOT NULL,
    longitude DOUBLE PRECISION,
    latitude DOUBLE PRECISION,
    image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_merchant_agencies_merchant FOREIGN KEY (merchant_id) REFERENCES merchants(id) ON DELETE CASCADE
);

-- Create index for merchant agency queries
CREATE INDEX idx_merchant_agencies_merchant_id ON merchant_agencies(merchant_id);
CREATE INDEX idx_merchant_agencies_location ON merchant_agencies(longitude, latitude);

-- =====================================================
-- Deal Tables
-- =====================================================

-- Merchant deals table
CREATE TABLE merchant_deals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    merchant_agency_id UUID NOT NULL,
    deal_name VARCHAR(255),
    discount_rate DOUBLE PRECISION,
    cashback_rate DOUBLE PRECISION,
    points_multiplier DOUBLE PRECISION,
    description VARCHAR(1000),
    valid_from DATE,
    valid_to DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_merchant_deals_merchant_agency FOREIGN KEY (merchant_agency_id) REFERENCES merchant_agencies(id) ON DELETE CASCADE
);

-- Create index for merchant deal queries
CREATE INDEX idx_merchant_deals_merchant_agency_id ON merchant_deals(merchant_agency_id);
CREATE INDEX idx_merchant_deals_valid_dates ON merchant_deals(valid_from, valid_to);

-- Card merchant deals (junction table for card_products and merchant_deals)
CREATE TABLE card_merchant_deals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    card_product_id UUID NOT NULL,
    merchant_deal_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_card_merchant_deals_card_product FOREIGN KEY (card_product_id) REFERENCES card_products(id) ON DELETE CASCADE,
    CONSTRAINT fk_card_merchant_deals_merchant_deal FOREIGN KEY (merchant_deal_id) REFERENCES merchant_deals(id) ON DELETE CASCADE
);

-- Create index for card merchant deal queries
CREATE INDEX idx_card_merchant_deals_card_product_id ON card_merchant_deals(card_product_id);
CREATE INDEX idx_card_merchant_deals_merchant_deal_id ON card_merchant_deals(merchant_deal_id);