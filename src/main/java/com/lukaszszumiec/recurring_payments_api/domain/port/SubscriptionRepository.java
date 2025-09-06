package com.lukaszszumiec.recurring_payments_api.domain.port;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    Subscription save(Subscription s);
    Optional<Subscription> findById(Long id);
    List<Subscription> findAllDueUntil(LocalDate date);
    List<Subscription> findAllByUserId(Long userId);
}
