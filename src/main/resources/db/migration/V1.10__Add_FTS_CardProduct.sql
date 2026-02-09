CREATE EXTENSION IF NOT EXISTS pg_trgm;


ALTER TABLE card_products
ADD COLUMN search_text TEXT
GENERATED ALWAYS AS (COALESCE(card_code, '') || ' ' || COALESCE(card_name, '') || ' ' || COALESCE(card_type, '')) STORED;



CREATE INDEX idx_card_products_search 
ON card_products 
USING GIN (search_text gin_trgm_ops);
ALTER DATABASE "ojt-postgres"
SET pg_trgm.similarity_threshold = 0.1;

