-- =====================================================
-- Flyway Migration V1.8: Create Subscribed and Favorite Merchants Tables
-- Description: Tables for tracking user subscriptions and favorites for merchant agencies
-- =====================================================

-- Subscribed merchants table (users subscribe to get notifications about deals)
CREATE TABLE subscribed_merchants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    merchant_agency_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_subscribed_merchants_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_subscribed_merchants_merchant_agency FOREIGN KEY (merchant_agency_id) REFERENCES merchant_agencies(id) ON DELETE CASCADE,
    CONSTRAINT uk_subscribed_merchants_user_agency UNIQUE (user_id, merchant_agency_id)
);

-- Favorite merchants table (users mark merchants as favorites for quick access)
CREATE TABLE favorite_merchants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    merchant_agency_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_favorite_merchants_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorite_merchants_merchant_agency FOREIGN KEY (merchant_agency_id) REFERENCES merchant_agencies(id) ON DELETE CASCADE,
    CONSTRAINT uk_favorite_merchants_user_agency UNIQUE (user_id, merchant_agency_id)
);