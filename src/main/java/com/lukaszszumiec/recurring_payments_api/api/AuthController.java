package com.lukaszszumiec.recurring_payments_api.api;

import com.lukaszszumiec.recurring_payments_api.infrastructure.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email,
                                                     @RequestParam String password,
                                                     @RequestParam(defaultValue = "USER") String role) {
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
                    .orElse(role);

            String token = jwtService.generateToken(email, Map.of(
                    "email", email,
                    "role", resolvedRole
            ));

            return ResponseEntity.ok(Map.of("accessToken", token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401).body(Map.of("error", "unauthorized"));
        }
        return ResponseEntity.ok(Map.of("username", auth.getName()));
    }


}