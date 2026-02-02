CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS pg_trgm;
-- EPSG:4326 is WGS 84 coordinate system 
ALTER TABLE merchant_agencies ADD COLUMN location GEOGRAPHY(POINT, 4326);

UPDATE merchant_agencies 
SET location = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326)::geography;


CREATE INDEX idx_merchants_name_trgm ON merchants USING GIN (name gin_trgm_ops);


CREATE INDEX idx_agencies_location ON merchant_agencies USING GIST (location);