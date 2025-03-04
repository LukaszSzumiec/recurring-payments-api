package com.lukaszszumiec.recurring_payments_api.application.usecase;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CancelSubscriptionUseCaseImpl implements CancelSubscriptionUseCase{

    private final SubscriptionRepository subscriptionRepository;

    public CancelSubscriptionUseCaseImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void cancelSubscription(UUID subscriptionId, User user) {
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findByIdAndUser(subscriptionId, user);
        if (subscriptionOptional.isPresent()){
            Subscription subscription = subscriptionOptional.get();
            subscription.setActive(false);
            subscriptionRepository.save(subscription);
        } else {
            throw new IllegalArgumentException("Subscription not found.");
        }
    }
}
