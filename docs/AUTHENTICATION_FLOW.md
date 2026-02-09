# Authentication Flow Documentation

---

## Overview

This application implements a JWT-based authentication system with multi-device support using a **Family Token** strategy—simply a way to handle multiple devices. Think of it as a unique ID for each user's device.

### Access and Refresh Tokens

Old school stuff, no need for much explanation here:

1. Extract access token from header
2. Validate the JWT
3. Extract claims (userId, role, familyToken from the payload)
4. Create principal (custom `UserPrincipal` with userId and familyToken)
5. Set it to `SecurityContextHolder`

```
┌─────────────────────────────────────────────────────────────┐
│                     Client Application                       │
└──────────────────┬──────────────────────────────────────────┘
                   │
                   │ HTTP Request with JWT
                   ▼
┌─────────────────────────────────────────────────────────────┐
│              JwtAuthenticationFilter                        │
│  - Extract access token from X-Access-Token header          │
│  - Validate token signature and expiration                  │
│  - Extract claims (userId, role, familyToken)               │
│  - Create UserPrincipal and set SecurityContext             │
└──────────────────┬──────────────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────────────┐
│           SecurityContextHolder                             │
│  Authentication: UsernamePasswordAuthenticationToken        │
│  ├─ Principal: UserPrincipal(userId, familyToken)           │
│  └─ Authorities: [ROLE_ADMIN | ROLE_CUSTOMER | ...]         │
└─────────────────────────────────────────────────────────────┘
```

---

## Family Token

A UUID for your device. That's it.

### Why Family Token?

**Multi-device support.**
- Obviously, I don't need this complexity for one user = one device
- But many users have multiple devices, so here we are

#### The Elevator Pitch

**"Without family_id, we lose the trace of the relationship between old (dead) tokens and new (alive) tokens."**

When we detect an old token being reused (Replay Attack), we know something's wrong, but we don't know which new token replaced it to revoke. Result: we're forced to logout ALL user devices (Kill All), affecting UX on devices that weren't compromised.

#### Deep Dive: Why Family Token Prevents "Kill All" Scenario

Let's visualize this with a concrete attack scenario:

**Setup:**
- User has 2 devices: Phone (Device A) and Laptop (Device B)
- Phone holds: `RT_A1`
- Laptop holds: `RT_B1`

**Normal Rotation:**
1. User refreshes on Phone → `RT_A1` dies, `RT_A2` is created
2. Valid tokens in DB/Redis: `[RT_A2, RT_B1]`

**Attack: Hacker Replay Attack**
Hacker stole `RT_A1` (Phone's old token) and tries to reuse it.

##### ❌ Case 1: WITHOUT family_id (Bad Approach)

**Detection:**
- Server receives `RT_A1`
- Check DB → `RT_A1` not found (or marked as `used`)
- Server detects: "Someone is reusing old token `RT_A1`! This is a hack!"

**The Million Dollar Question:**
- Server looks at valid tokens: `[RT_A2, RT_B1]`
- How does Server know `RT_A2` is the "child" of `RT_A1` to revoke it?

**Dead End:**
- Without `family_id` or any linkage, Server cannot tell that `RT_A1` and `RT_A2` belong to the same chain
- Server also doesn't know if `RT_A1` is related to `RT_B1` or not

**Consequence:**
- To be safe, Server **revokes EVERYTHING**: `RT_A2` and `RT_B1`
- ⚠️ User on Laptop (Device B) suddenly gets kicked out → **Bad UX**

##### ✅ Case 2: WITH family_id (Our Approach)

**Data Structure:**
```
Chain A (Phone):    family_id: family_X  →  Current: RT_A2
Chain B (Laptop):   family_id: family_Y  →  Current: RT_B1
```

**Attack Handling:**
1. Hacker sends `RT_A1`
2. Server decodes `RT_A1`, finds `family_token: family_X` inside
3. Server queries `family_X`. Detects current token should be `RT_A2`, not `RT_A1` → **Suspicious!**
4. Server revokes **only** `family_X` chain

**Result:**
- `RT_A2` (Phone's current token) is revoked → Phone gets logged out ✅ (Correct, Phone is compromised)
- `family_Y` (Laptop) is completely innocent. `RT_B1` remains valid ✅
- → **Good UX & Security**: Only isolate the infected device

**Key Insight:**
`family_id` acts as a **chain identifier** that links all token generations of a single device, enabling **surgical revocation** instead of nuclear "kill all" approach.

### Scenarios

#### 1. Login
- Each time you log in on a new device, the server generates a UUID for that device (stored in the JWT payload—not super safe, but at least I feel less guilty)
- Why this helps: when you logout, I don't kick all your other devices
- During login, you can provide a refresh token in the `X-Refresh-Token` header (if re-logging in) to help me revoke the old refresh token from your device
- If you don't provide it, I assume: new device, first-time login, or same device but different account

#### 2. Register
- New family token is **always** generated, regardless of device

#### 3. Login with Google
- First-time login with 3rd party authentication is treated the same as a first-time regular login

#### 4. Logout
- Delete all refresh tokens with the given family token (found in `SecurityContextHolder`)

#### 5. Hacker Stole Refresh Token
Security rules to handle this:
- Each refresh token can only be used **once**. Second usage → Suspicious activity detected
- What if the hacker uses the stolen token first?
  - Don't worry—when you try to use your token, the system detects suspicious activity and requires re-login
  - You only need to re-login on the device with the stolen refresh token; other devices are safe
