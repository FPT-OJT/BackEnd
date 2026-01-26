-- KEYS[1]: Redis key for the refresh token (e.g., "refresh_tokens:token_value")
-- ARGV[1]: Field name to check revoked status (e.g., "isRevoked")
-- ARGV[2]: Expected value for non-revoked status (e.g., "0" or "false")

-- Returns:
--   1 = Successfully revoked (token was valid and now revoked)
--   0 = Already revoked (token was already used - potential replay attack)
--  -1 = Token not found (expired or invalid)

-- Check if token exists
local exists = redis.call('EXISTS', KEYS[1])
if exists == 0 then
    return -1
end

-- Get current revoked status
local current_status = redis.call('HGET', KEYS[1], ARGV[1])

-- Check if already revoked
if current_status ~= ARGV[2] then
    return 0  -- Already revoked
end

-- Atomically mark as revoked
redis.call('HSET', KEYS[1], ARGV[1], '1')

return 1  -- Successfully revoked
