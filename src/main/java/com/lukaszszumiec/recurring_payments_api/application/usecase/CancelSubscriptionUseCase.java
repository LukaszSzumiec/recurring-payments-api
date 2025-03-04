package com.lukaszszumiec.recurring_payments_api.application.usecase;

import com.lukaszszumiec.recurring_payments_api.domain.model.User;

import java.util.UUID;

public interface CancelSubscriptionUseCase {
    void cancelSubscription(UUID subscriptionId, User user);
}
