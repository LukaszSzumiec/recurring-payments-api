package com.lukaszszumiec.recurring_payments_api.application.usecase;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;

public interface ProcessPaymentUseCase {
    void processForSubscription(Subscription subscription);
}
