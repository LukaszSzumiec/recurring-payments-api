package com.lukaszszumiec.recurring_payments_api.domain.port;

import com.lukaszszumiec.recurring_payments_api.domain.model.OutboxEvent;

import java.util.List;

public interface OutboxRepository {
    OutboxEvent save(OutboxEvent event);

    List<OutboxEvent> findPending(int limit);

    void markSent(Long id);

    void markFailed(Long id, int attempts);
}
