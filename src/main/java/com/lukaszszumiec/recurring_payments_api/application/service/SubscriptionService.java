package com.lukaszszumiec.recurring_payments_api.application.service;

import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionRequest;
import com.lukaszszumiec.recurring_payments_api.application.usecase.CancelSubscriptionUseCase;
import com.lukaszszumiec.recurring_payments_api.application.usecase.CreateSubscriptionUseCase;
import com.lukaszszumiec.recurring_payments_api.application.usecase.GetSubscriptionsUseCase;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final CreateSubscriptionUseCase createSubscriptionUseCase;
    private final GetSubscriptionsUseCase getSubscriptionsUseCase;
    private final CancelSubscriptionUseCase cancelSubscriptionUseCase;

    public SubscriptionService(CreateSubscriptionUseCase createSubscriptionUseCase, GetSubscriptionsUseCase getSubscriptionsUseCase, CancelSubscriptionUseCase cancelSubscriptionUseCase) {
        this.createSubscriptionUseCase = createSubscriptionUseCase;
        this.getSubscriptionsUseCase = getSubscriptionsUseCase;
        this.cancelSubscriptionUseCase = cancelSubscriptionUseCase;
    }

    public List<Subscription> getSubscriptions(User user){
        return getSubscriptionsUseCase.getActiveSubscriptions(user);
    }

    public Subscription createSubscription(User user, CreateSubscriptionRequest request){
        return createSubscriptionUseCase.createSubscription(user, request);
    }

    public void cancelSubscription(UUID subscriptionId, User user){
        cancelSubscriptionUseCase.cancelSubscription(subscriptionId, user);
    }
}
