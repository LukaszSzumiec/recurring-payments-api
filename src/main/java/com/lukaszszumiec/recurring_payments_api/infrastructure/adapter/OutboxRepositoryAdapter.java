package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter;

import com.lukaszszumiec.recurring_payments_api.domain.model.OutboxEvent;
import com.lukaszszumiec.recurring_payments_api.domain.port.OutboxRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxRepositoryAdapter
        extends JpaRepository<OutboxEvent, UUID>, OutboxRepository {


    @Query("select o from OutboxEvent o where o.status = :status order by o.id asc")
    List<OutboxEvent> findByStatusOrderByIdAsc(@Param("status") String status,
                                               org.springframework.data.domain.Pageable pageable);

    @Modifying
    @Query("update OutboxEvent o set o.status = 'SENT', o.lastAttemptAt = :ts where o.id = :id")
    int updateMarkSent(@Param("id") Long id, @Param("ts") LocalDateTime ts);

    @Modifying
    @Query("update OutboxEvent o set o.status = 'FAILED', o.attempts = :attempts, o.lastAttemptAt = :ts where o.id = :id")
    int updateMarkFailed(@Param("id") Long id, @Param("attempts") int attempts, @Param("ts") LocalDateTime ts);

    // --- Implementacja portu (default methods) ---

    @Override
    default OutboxEvent store(OutboxEvent event) {
        // Tak samo jak w RefreshTokenRepositoryAdapter: używamy save(...) z JpaRepository
        return save(event);
    }

    @Override
    default List<OutboxEvent> findPending(int limit) {
        return findByStatusOrderByIdAsc("PENDING", PageRequest.of(0, limit));
    }

    @Override
    default void markSent(Long id) {
        updateMarkSent(id, LocalDateTime.now());
    }

    @Override
    default void markFailed(Long id, int attempts) {
        updateMarkFailed(id, attempts, LocalDateTime.now());
    }
}
