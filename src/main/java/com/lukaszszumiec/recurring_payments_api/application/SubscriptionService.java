package com.lukaszszumiec.recurring_payments_api.application;

import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionCommand;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;

public interface SubscriptionService {
    Subscription create(CreateSubscriptionCommand cmd);
}
