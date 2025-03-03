package com.lukaszszumiec.recurring_payments_api.application.service;

import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionRequest;
import com.lukaszszumiec.recurring_payments_api.application.usecase.CreateSubscriptionUseCase;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
    private final CreateSubscriptionUseCase createSubscriptionUseCase;


    public SubscriptionService(CreateSubscriptionUseCase createSubscriptionUseCase) {
        this.createSubscriptionUseCase = createSubscriptionUseCase;
    }

    public Subscription createSubscription(User user, CreateSubscriptionRequest request){
        return createSubscriptionUseCase.createSubscription(user, request);
    }
}
