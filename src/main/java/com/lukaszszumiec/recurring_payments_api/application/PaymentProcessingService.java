package com.lukaszszumiec.recurring_payments_api.application;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;

public interface PaymentProcessingService {
    void processForSubscription(Subscription subscription);
}
