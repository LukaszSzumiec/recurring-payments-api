package com.lukaszszumiec.recurring_payments_api.infrastructure.security;

import com.lukaszszumiec.recurring_payments_api.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {
    private final Key key;
    private final long accessMs;
    private final long refreshMs;

    public JwtService(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(props.secret().getBytes());
        this.accessMs = props.expiration() * 1000;
        this.refreshMs = props.refreshExpiration() * 1000;
    }

    public String generateAccessToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .claim("typ", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessMs))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username, String jti, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("typ", "refresh")
                .claim("jti", jti)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshMs))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token);
    }

    public String extractUsername(String token) {
        return parseToken(token).getPayload().getSubject();
    }

    public String extractType(String token) {
        Object t = parseToken(token).getPayload().get("typ");
        return t == null ? null : t.toString();
    }

    public String extractJti(String token) {
        Object j = parseToken(token).getPayload().get("jti");
        return j == null ? null : j.toString();
    }

    public String extractRole(String token) {
        Object r = parseToken(token).getPayload().get("role");
        return r == null ? null : r.toString();
    }

    public String newJti() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
