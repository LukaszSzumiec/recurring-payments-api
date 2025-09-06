package com.lukaszszumiec.recurring_payments_api.domain.port;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository {

    void store(Long userId, String jti, Instant issuedAt, Instant expiresAt);

    void revoke(String jti);

    void revokeAllForUser(Long userId);

    boolean isActive(String jti, Instant now);

    Optional<Instant> expiresAt(String jti);
}
