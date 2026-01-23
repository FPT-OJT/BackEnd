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
