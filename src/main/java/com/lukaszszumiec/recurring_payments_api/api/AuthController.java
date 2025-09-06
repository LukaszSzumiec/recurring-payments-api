package com.lukaszszumiec.recurring_payments_api.api;

import com.lukaszszumiec.recurring_payments_api.generated.api.AuthApi;
import com.lukaszszumiec.recurring_payments_api.generated.model.TokenResponseDto;
import com.lukaszszumiec.recurring_payments_api.generated.model.UsernameResponseDto;
import com.lukaszszumiec.recurring_payments_api.infrastructure.security.JwtService;
import com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.RefreshTokenRepositoryAdapter;
import com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.UserRepositoryAdapter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
public class AuthController implements AuthApi {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepositoryAdapter refreshTokens;
    private final UserRepositoryAdapter users;

    public AuthController(JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          RefreshTokenRepositoryAdapter refreshTokens,
                          UserRepositoryAdapter users) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokens = refreshTokens;
        this.users = users;
    }

    @Override
    public ResponseEntity<TokenResponseDto> login(String email, String password, String role) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            String resolvedRole = auth.getAuthorities().stream()
                    .findFirst()
                    .map(a -> {
                        String r = a.getAuthority();
                        return r != null && r.startsWith("ROLE_") ? r.substring(5) : r;
                    })
                    .orElse(role != null ? role : "USER");

            var user = users.findByEmail(email).orElseThrow();

            String access = jwtService.generateAccessToken(email, Map.of(
                    "email", email,
                    "role", resolvedRole
            ));

            String jti = jwtService.newJti();
            String refresh = jwtService.generateRefreshToken(email, jti, resolvedRole);
            var refreshExp = jwtService.parseToken(refresh).getPayload().getExpiration().toInstant();
            refreshTokens.store(user.getId(), jti, Instant.now(), refreshExp);

            Duration maxAge = Duration.between(Instant.now(), refreshExp).isNegative() ? Duration.ZERO : Duration.between(Instant.now(), refreshExp);
            ResponseCookie cookie = ResponseCookie.from("refresh_token", refresh)
                    .httpOnly(true)
                    .secure(true)
                    .path("/api/auth")
                    .sameSite("Strict")
                    .maxAge(maxAge)
                    .build();

            TokenResponseDto tr = new TokenResponseDto();
            tr.setAccessToken(access);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(tr);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@CookieValue(name = "refresh_token", required = false) String cookieToken,
                                                    @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authz) {
        String token = cookieToken;
        if ((token == null || token.isBlank()) && authz != null && authz.startsWith("Bearer ")) {
            token = authz.substring(7);
        }
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        try {
            if (!"refresh".equalsIgnoreCase(jwtService.extractType(token))) {
                return ResponseEntity.status(401).build();
            }
            String jti = jwtService.extractJti(token);
            var now = Instant.now();
            if (!refreshTokens.isActive(jti, now)) {
                return ResponseEntity.status(401).build();
            }

            String email = jwtService.extractUsername(token);
            var user = users.findByEmail(email).orElseThrow();
            String role = jwtService.extractRole(token);
            if (role == null || role.isBlank()) role = "USER";

            String access = jwtService.generateAccessToken(email, Map.of(
                    "email", email,
                    "role", role
            ));

            refreshTokens.revoke(jti);
            String newJti = jwtService.newJti();
            String newRefresh = jwtService.generateRefreshToken(email, newJti, role);
            var newExp = jwtService.parseToken(newRefresh).getPayload().getExpiration().toInstant();
            refreshTokens.store(user.getId(), newJti, now, newExp);

            Duration maxAge = Duration.between(Instant.now(), newExp).isNegative() ? Duration.ZERO : Duration.between(Instant.now(), newExp);
            ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefresh)
                    .httpOnly(true)
                    .secure(true)
                    .path("/api/auth")
                    .sameSite("Strict")
                    .maxAge(maxAge)
                    .build();

            TokenResponseDto tr = new TokenResponseDto();
            tr.setAccessToken(access);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(tr);
        } catch (Exception e) {
            ResponseCookie clear = ResponseCookie.from("refresh_token", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/api/auth")
                    .sameSite("Strict")
                    .maxAge(Duration.ZERO)
                    .build();
            return ResponseEntity.status(401).header(HttpHeaders.SET_COOKIE, clear.toString()).build();
        }
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", required = false) String cookieToken,
                                       @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authz) {
        String token = cookieToken;
        if ((token == null || token.isBlank()) && authz != null && authz.startsWith("Bearer ")) {
            token = authz.substring(7);
        }
        if (token != null && !token.isBlank()) {
            try {
                if ("refresh".equalsIgnoreCase(jwtService.extractType(token))) {
                    String jti = jwtService.extractJti(token);
                    refreshTokens.revoke(jti);
                }
            } catch (Exception ignored) {}
        }
        ResponseCookie clear = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .sameSite("Strict")
                .maxAge(Duration.ZERO)
                .build();
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, clear.toString()).build();
    }

    @Override
    public ResponseEntity<UsernameResponseDto> getCurrentUser() {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }
        UsernameResponseDto ur = new UsernameResponseDto();
        ur.setUsername(auth.getName());
        return ResponseEntity.ok(ur);
    }
}
