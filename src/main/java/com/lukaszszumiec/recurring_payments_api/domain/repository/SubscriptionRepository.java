package com.lukaszszumiec.recurring_payments_api.domain.repository;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;

import java.util.UUID;

public interface SubscriptionRepository extends BaseRepository <Subscription, UUID> {

}
