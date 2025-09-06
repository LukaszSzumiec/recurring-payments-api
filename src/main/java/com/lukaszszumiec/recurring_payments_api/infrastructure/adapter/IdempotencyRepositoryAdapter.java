package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter;

import com.lukaszszumiec.recurring_payments_api.domain.model.IdempotencyKey;
import com.lukaszszumiec.recurring_payments_api.domain.port.IdempotencyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdempotencyRepositoryAdapter extends JpaRepository<IdempotencyKey, Long>, IdempotencyRepository {
    Optional<IdempotencyKey> findByKey(String key);
}
