package com.lukaszszumiec.recurring_payments_api.application.usecase;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetSubscriptionsUseCaseImpl implements GetSubscriptionsUseCase {

    private final SubscriptionRepository subscriptionRepository;

    public GetSubscriptionsUseCaseImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Subscription> getActiveSubscriptions(User user) {
        return subscriptionRepository.findByUserAndActiveTrue(user);
    }
}
