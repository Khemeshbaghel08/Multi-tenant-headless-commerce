package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.digest.DigestUtils;


import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Component
public class JwtService {

    private static final long ACCESS_TOKEN_MS = 15 * 60 * 1000;

    @Autowired
    private PrivateKey privateKey;
    @Autowired
    private PublicKey publicKey;


    public Claims validateToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String generateAccessToken(String userId, String tenantId) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("tenantId", tenantId)
                .claim("role", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_MS))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(privateKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public String getUserId(String token) {
        return validateToken(token).getSubject();
    }

    public String getTenantId(String token) {
        return validateToken(token).get("tenantId", String.class);
    }

    public String hashToken(String token) {
        return DigestUtils.sha256Hex(token);
    }


}
