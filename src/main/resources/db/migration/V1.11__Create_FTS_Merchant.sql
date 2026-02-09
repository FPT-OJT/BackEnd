CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS pg_trgm;
-- EPSG:4326 is WGS 84 coordinate system 
ALTER TABLE merchant_agencies ADD COLUMN location GEOGRAPHY(POINT, 4326);

UPDATE merchant_agencies 
SET location = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326)::geography;


CREATE INDEX idx_merchants_name_trgm ON merchants USING GIN (name gin_trgm_ops);


CREATE INDEX idx_agencies_location ON merchant_agencies USING GIST (location);


ALTER TABLE merchant_agencies
ADD COLUMN search_text TEXT;

-- Update search text for existing agencies
UPDATE merchant_agencies ma
SET search_text =
  COALESCE(ma.name, '') || ' ' || COALESCE(m.name, '')
FROM merchants m
WHERE m.id = ma.merchant_id;

CREATE OR REPLACE FUNCTION update_search_text()
RETURNS trigger AS $$
BEGIN
  NEW.search_text :=
    COALESCE(NEW.name, '') || ' ' ||
    COALESCE(
      (SELECT m.name FROM merchants m WHERE m.id = NEW.merchant_id),
      ''
    );

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_search_text_trigger
BEFORE INSERT OR UPDATE ON merchant_agencies
FOR EACH ROW
EXECUTE FUNCTION update_search_text();
