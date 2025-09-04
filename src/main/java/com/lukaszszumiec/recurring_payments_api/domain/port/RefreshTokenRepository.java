package com.lukaszszumiec.recurring_payments_api.domain.port;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    void store(UUID userId, String jti, Instant issuedAt, Instant expiresAt);

    void revoke(String jti);

    void revokeAllForUser(UUID userId);

    boolean isActive(String jti, Instant now);

    Optional<Instant> expiresAt(String jti);
}
