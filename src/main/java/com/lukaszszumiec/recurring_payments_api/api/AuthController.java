package com.lukaszszumiec.recurring_payments_api.api;

import com.lukaszszumiec.recurring_payments_api.generated.api.AuthApi;
import com.lukaszszumiec.recurring_payments_api.generated.model.LoginFormDto;
import com.lukaszszumiec.recurring_payments_api.generated.model.TokenResponseDto;
import com.lukaszszumiec.recurring_payments_api.generated.model.UsernameResponseDto;
import com.lukaszszumiec.recurring_payments_api.infrastructure.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController implements AuthApi {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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

            String token = jwtService.generateToken(email, Map.of(
                    "email", email,
                    "role", resolvedRole
            ));

            TokenResponseDto tr = new TokenResponseDto();
            tr.setAccessToken(token);
            return ResponseEntity.ok(tr);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).build();
        }
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
