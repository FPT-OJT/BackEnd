drop FUNCTION if EXISTS get_user_active_cards;
CREATE OR REPLACE FUNCTION get_user_active_cards(p_user_id UUID)
RETURNS TABLE (
    user_card_id UUID,
    card_product_id UUID,
    card_name VARCHAR,
    card_code VARCHAR,
    card_type VARCHAR,
    expiry_date DATE,
    first_payment_date INT,
    added_at TIMESTAMP
)
LANGUAGE sql
STABLE
AS $$
    SELECT
        ucc.id                     AS user_card_id,
        cp.id                      AS card_product_id,
        cp.card_name,
        cp.card_code,
        cp.card_type,

        ucc.expiry_date,
        ucc.first_payment_date,
        ucc.created_at             AS added_at
    FROM user_credit_cards ucc
    JOIN card_products cp
        ON cp.id = ucc.card_product_id
    WHERE ucc.user_id = p_user_id
      AND ucc.deleted_at IS NULL
      AND cp.deleted_at IS NULL
      AND (
            ucc.expiry_date IS NULL
            OR ucc.expiry_date >= CURRENT_DATE
          )
    ORDER BY ucc.created_at DESC;
$$;
DROP FUNCTION if EXISTS get_nearby_merchants;
CREATE OR REPLACE FUNCTION get_nearby_merchants(
    p_lat DOUBLE PRECISION,
    p_lng DOUBLE PRECISION,
    p_limit INT DEFAULT 10
)
RETURNS TABLE (
    brand_name VARCHAR,
    agency_name VARCHAR,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    description VARCHAR,
    distance_meters DOUBLE PRECISION
)
LANGUAGE sql
STABLE
AS $$
    WITH user_point AS (
        SELECT ST_SetSRID(
                   ST_MakePoint(p_lng, p_lat),
                   4326
               )::geography AS point
    )
    SELECT
        m.name                                AS brand_name,
        a.name                                AS agency_name,
        a.latitude,
        a.longitude,
        m.description,
        ROUND(
            ST_Distance(a.location, u.point)::numeric,
            2
        )                                     AS distance_meters
    FROM merchant_agencies a
    JOIN merchants m
        ON a.merchant_id = m.id
    CROSS JOIN user_point u
    WHERE a.deleted_at IS NULL
      AND m.deleted_at IS NULL
      AND a.location IS NOT NULL
    ORDER BY a.location <-> u.point
    LIMIT COALESCE(p_limit, 10);
$$;
drop FUNCTION if EXISTS get_all_table_schema;
drop FUNCTION if EXISTS get_table_schema;
CREATE OR REPLACE FUNCTION get_all_table_schema(p_schema text DEFAULT 'public')
RETURNS TABLE (
  table_name text,
  fields json
)
LANGUAGE sql
AS $$
  SELECT
    c.relname AS table_name,
    json_agg(
      json_build_object(
        'name', a.attname,
        'datatype', format_type(a.atttypid, a.atttypmod)
      )
      ORDER BY a.attnum
    ) AS fields
  FROM pg_attribute a
  JOIN pg_class c     ON c.oid = a.attrelid
  JOIN pg_namespace n ON n.oid = c.relnamespace
  WHERE n.nspname = p_schema
    AND c.relkind = 'r'
    AND a.attnum > 0
    AND NOT a.attisdropped
  GROUP BY c.relname
  ORDER BY c.relname;
$$;



DROP FUNCTION IF EXISTS get_user_active_cards_with_benefits;
CREATE OR REPLACE FUNCTION get_user_active_cards_with_benefits(p_user_id UUID)
RETURNS TABLE (
    -- card info
    card_name                       VARCHAR,
    card_code                       VARCHAR,
    card_type                       VARCHAR,
    expiry_date                     DATE,
    first_payment_date              INT,
    added_at                        TIMESTAMP,

    -- card rules
    match_conditions                JSONB,
    match_allow_mccs                JSONB,
    match_reject_mccs               JSONB,
    effect_cashback_rate            FLOAT8,
    effect_points_rate              FLOAT8,
    effect_rebate_rate              FLOAT8,
    effect_merchant_discount_rate   FLOAT8,
    effect_fee_rate                 FLOAT8
)
LANGUAGE sql
STABLE
AS $$
    SELECT
        cp.card_name,
        cp.card_code,
        cp.card_type,
        ucc.expiry_date,
        ucc.first_payment_date,
        ucc.created_at                  AS added_at,

        cr.match_conditions,
        cr.match_allow_mccs,
        cr.match_reject_mccs,
        cr.effect_cashback_rate,
        cr.effect_points_rate,
        cr.effect_rebate_rate,
        cr.effect_merchant_discount_rate,
        cr.effect_fee_rate

    FROM user_credit_cards ucc
    JOIN card_products cp
        ON cp.id = ucc.card_product_id
        AND cp.deleted_at IS NULL
    LEFT JOIN card_rules cr
        ON cr.card_product_id = cp.id
        AND cr.deleted_at IS NULL
    WHERE ucc.user_id = p_user_id
      AND ucc.deleted_at IS NULL
      AND (
            ucc.expiry_date IS NULL
            OR ucc.expiry_date >= CURRENT_DATE
          )
    ORDER BY ucc.created_at DESC, cr.created_at ASC;
$$;

-- SELECT * FROM get_user_active_cards_with_benefits('e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b');
DROP FUNCTION IF EXISTS get_nearby_deals;
CREATE OR REPLACE FUNCTION get_nearby_deals(
    p_lat   DOUBLE PRECISION,
    p_lng   DOUBLE PRECISION,
    p_limit INT DEFAULT 10
)
RETURNS TABLE (
    brand_name          VARCHAR,
    agency_name         VARCHAR,
    distance_meters     DOUBLE PRECISION,
    deal_name           VARCHAR,
    description         VARCHAR,
    discount_rate       FLOAT8,
    cashback_rate       FLOAT8,
    points_multiplier   FLOAT8,
    valid_from          DATE,
    valid_to            DATE
)
LANGUAGE sql
STABLE
AS $$
    WITH user_point AS (
        SELECT ST_SetSRID(
                   ST_MakePoint(p_lng, p_lat),
                   4326
               )::geography AS point
    ),
    nearby_agencies AS (
        SELECT
            a.id,
            m.name  AS brand_name,
            a.name  AS agency_name,
            ROUND(ST_Distance(a.location, u.point)::numeric, 2) AS distance_meters
        FROM merchant_agencies a
        JOIN merchants m
            ON a.merchant_id = m.id
        CROSS JOIN user_point u
        WHERE a.deleted_at IS NULL
          AND m.deleted_at IS NULL
          AND a.location IS NOT NULL
        ORDER BY a.location <-> u.point
        LIMIT COALESCE(p_limit, 10)
    )
    SELECT
        na.brand_name,
        na.agency_name,
        na.distance_meters,
        md.deal_name,
        md.description,
        md.discount_rate,
        md.cashback_rate,
        md.points_multiplier,
        md.valid_from,
        md.valid_to
    FROM nearby_agencies na
    JOIN merchant_deals md
        ON md.merchant_agency_id = na.id
        AND md.deleted_at IS NULL
        AND (md.valid_from IS NULL OR md.valid_from <= CURRENT_DATE)
        AND (md.valid_to   IS NULL OR md.valid_to   >= CURRENT_DATE)
    ORDER BY na.distance_meters ASC, md.cashback_rate DESC NULLS LAST;
$$;
-- select * from get_nearby_deals(10.835369,106.807604)
