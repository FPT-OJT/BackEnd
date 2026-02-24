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