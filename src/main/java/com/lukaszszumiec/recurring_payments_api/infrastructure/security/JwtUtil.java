package com.lukaszszumiec.recurring_payments_api.infrastructure.security;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {
    private static final String SECRET_KEY = "Secret-key";
    private static final long EXPIRATION_TIME = 86400000;

    private Key getSignedKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractEmail(String token){
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignedKey()).build().parseClaimsJws(token).getBody();
    }
}
