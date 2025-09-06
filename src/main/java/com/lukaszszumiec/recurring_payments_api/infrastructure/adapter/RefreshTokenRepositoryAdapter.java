package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter;

import com.lukaszszumiec.recurring_payments_api.domain.model.RefreshTokenEntity;
import com.lukaszszumiec.recurring_payments_api.domain.port.RefreshTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepositoryAdapter extends JpaRepository<RefreshTokenEntity, UUID>, RefreshTokenRepository {

    Optional<RefreshTokenEntity> findByJti(String jti);

    @Override
    default void store(Long userId, String jti, Instant issuedAt, Instant expiresAt) {
        save(new RefreshTokenEntity(UUID.randomUUID(), userId, jti, issuedAt, expiresAt, false));
    }

    @Override
    default void revoke(String jti) {
        findByJti(jti).ifPresent(rt -> { rt.setRevoked(true); save(rt); });
    }

    @Override
    default void revokeAllForUser(Long userId) {
        findAll().stream()
                .filter(rt -> userId.equals(rt.getUserId()))
                .filter(rt -> !rt.isRevoked())
                .forEach(rt -> { rt.setRevoked(true); save(rt); });
    }

    @Override
    default boolean isActive(String jti, Instant now) {
        return findByJti(jti)
                .filter(rt -> !rt.isRevoked())
                .filter(rt -> now.isBefore(rt.getExpiresAt()))
                .isPresent();
    }

    @Override
    default Optional<Instant> expiresAt(String jti) {
        return findByJti(jti).map(RefreshTokenEntity::getExpiresAt);
    }
}
