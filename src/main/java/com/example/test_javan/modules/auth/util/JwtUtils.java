package com.example.test_javan.modules.auth.util;

import com.example.test_javan.modules.auth.TokenTypeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    @Value("${jwt.refreshSecret}")
    private String jwtRefreshSecret;

    @Value("${jwt.refreshExpirationMs}")
    private long jwtRefreshExpirationMs;

    // ================== KEY HELPERS ==================
    private Key getAccessKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private Key getRefreshKey() {
        return Keys.hmacShaKeyFor(jwtRefreshSecret.getBytes());
    }

    // ================== ACCESS TOKEN ==================
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", TokenTypeEnum.ACCESS.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getAccessKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, getAccessKey(), TokenTypeEnum.ACCESS);
    }

    public String getUsernameFromAccessToken(String token) {
        return getUsernameFromToken(token, getAccessKey());
    }

    // ================== REFRESH TOKEN ==================
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", TokenTypeEnum.REFRESH.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(getRefreshKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, getRefreshKey(), TokenTypeEnum.REFRESH);
    }

    public String getUsernameFromRefreshToken(String token) {
        return getUsernameFromToken(token, getRefreshKey());
    }

    // ================== COMMON ==================
    private boolean validateToken(String token, Key key, TokenTypeEnum expectedType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String typeClaim = claims.get("type", String.class);
            return expectedType.name().equals(typeClaim);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private String getUsernameFromToken(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public TokenTypeEnum getTokenTypeEnum(String token, boolean isRefreshToken) {
        Key key = isRefreshToken ? getRefreshKey() : getAccessKey();
        try {
            String type = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("type", String.class);
            return TokenTypeEnum.valueOf(type);
        } catch (Exception e) {
            return null;
        }
    }
}
