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

-- Handle nil/missing field (treat as revoked for safety)
if not current_status then
    return 0  -- Field missing, treat as already revoked
end

-- Check if already revoked (compare with expected non-revoked value)
if current_status ~= ARGV[2] then
    return 0  -- Already revoked or unexpected value
end

-- Atomically mark as revoked (set to "1" for Spring Data Redis boolean true)
redis.call('HSET', KEYS[1], ARGV[1], '1')

return 1
