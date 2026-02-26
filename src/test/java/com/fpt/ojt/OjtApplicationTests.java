package com.fpt.ojt;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class OjtApplicationTests {

    // @MockitoBean
    // private RefreshTokenRepository refreshTokenRepository;

    // @MockitoBean
    // private PasswordResetTokenRepository passwordResetTokenRepository;

    // @MockitoBean
    // private RedisTemplate<String, Object> redisTemplate;

    // @MockitoBean
    // private RedisScript<Long> revokeTokenScript;

    // @Test
    // void contextLoads() {
    // }

}
