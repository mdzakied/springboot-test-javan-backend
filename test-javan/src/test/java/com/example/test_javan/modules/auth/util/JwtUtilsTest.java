package com.example.test_javan.modules.auth.util;

import com.example.test_javan.modules.auth.TokenTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        // Inject value from application.properties
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "dmVyeXNlY3JldGtleXZlcnlzZWNyZXRrZXl2ZXJ5c2VjcmV0a2V5dmVyeXNlY3JldGtleQ==");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000L); // 1h
        ReflectionTestUtils.setField(jwtUtils, "jwtRefreshSecret", "dmVyeXJlZnJlc2hzZWNyZXRrZXl2ZXJ5cmVmcmVzaHNlY3JldGtleXZlcnlyZWZyZXNo");
        ReflectionTestUtils.setField(jwtUtils, "jwtRefreshExpirationMs", 7200000L); // 2h
    }

    @Test
    void generateAndValidateAccessToken() {
        // Mock data
        String username = "john";

        // Act
        String token = jwtUtils.generateAccessToken(username);

        // Assert
        assertThat(token).isNotBlank();
        assertThat(jwtUtils.validateAccessToken(token)).isTrue();
        assertThat(jwtUtils.getUsernameFromAccessToken(token)).isEqualTo(username);
        assertThat(jwtUtils.getTokenTypeEnum(token, false)).isEqualTo(TokenTypeEnum.ACCESS);
    }

    @Test
    void generateAndValidateRefreshToken() {
        // Mock data
        String username = "doe";

        // Act
        String token;
        token = jwtUtils.generateRefreshToken(username);

        // Assert
        assertThat(token).isNotBlank();
        assertThat(jwtUtils.validateRefreshToken(token)).isTrue();
        assertThat(jwtUtils.getUsernameFromRefreshToken(token)).isEqualTo(username);
        assertThat(jwtUtils.getTokenTypeEnum(token, true)).isEqualTo(TokenTypeEnum.REFRESH);
    }

    @Test
    void validateAccessToken_withWrongType_shouldReturnFalse() {
        // Mock data
        String refreshToken = jwtUtils.generateRefreshToken("john");

        // Act & Assert → failure process / false
        assertThat(jwtUtils.validateAccessToken(refreshToken)).isFalse();
    }

    @Test
    void validateToken_withInvalidSignature_shouldReturnFalse() {
        // Mock data
        String fakeToken = "eyJhbGciOiJIUzUxMiJ9.fake.payload";

        // Act & Assert → failure process / false
        assertThat(jwtUtils.validateAccessToken(fakeToken)).isFalse();
        assertThat(jwtUtils.validateRefreshToken(fakeToken)).isFalse();
    }

    @Test
    void getTokenTypeEnum_withInvalidToken_shouldReturnNull() {
        // Mock data
        String fakeToken = "eyJhbGciOiJIUzUxMiJ9.fake.payload";

        // Act
        TokenTypeEnum type = jwtUtils.getTokenTypeEnum(fakeToken, false);

        // Assert
        assertThat(type).isNull();
    }
}
