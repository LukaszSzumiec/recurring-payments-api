package com.lukaszszumiec.recurring_payments_api.application.usecase;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;

public interface CreateSubscriptionUseCase {
    Subscription create(CreateSubscriptionCommand cmd);

    record CreateSubscriptionCommand(Long userId, String price, Integer billingDayOfMonth) {
    }
}
