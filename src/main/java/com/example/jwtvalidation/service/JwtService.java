package com.example.jwtvalidation.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret}") String base64Secret) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // Step 1: Utility method - verifies signature and expiry, then extracts claims.
    public Map<String, Object> validateAndExtractClaims(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        Map<String, Object> extracted = new LinkedHashMap<>();
        extracted.put("subject", claims.getSubject());
        extracted.put("email", claims.get("email", String.class));
        extracted.put("role", claims.get("role", String.class));
        extracted.put("issuedAt", claims.getIssuedAt());
        extracted.put("expiration", claims.getExpiration());
        extracted.put("allClaims", claims);
        return extracted;
    }

    // Demo helper: creates tokens so the practical can be tested from Swagger.
    public String generateDemoToken(String subject, String email, String role, long validitySeconds) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validitySeconds * 1000);

        return Jwts.builder()
            .subject(subject)
            .claim("email", email)
            .claim("role", role)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact();
    }
}
