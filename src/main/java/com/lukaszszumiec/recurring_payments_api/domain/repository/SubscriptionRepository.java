package com.lukaszszumiec.recurring_payments_api.domain.repository;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends BaseRepository <Subscription, UUID> {
    List<Subscription> findByUserAndActiveTrue(User user);
    Optional<Subscription> findByIdAndUser(UUID id, User user);
}
