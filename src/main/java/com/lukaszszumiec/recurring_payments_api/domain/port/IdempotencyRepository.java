package com.lukaszszumiec.recurring_payments_api.domain.port;

import com.lukaszszumiec.recurring_payments_api.domain.model.IdempotencyKey;

import java.util.Optional;

public interface IdempotencyRepository {
    Optional<IdempotencyKey> findByKey(String key);
    IdempotencyKey save(IdempotencyKey rec);
}
