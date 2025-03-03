package com.lukaszszumiec.recurring_payments_api.application.usecase;

import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionRequest;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CreateSubscriptionUseCaseImpl implements CreateSubscriptionUseCase {
    private final SubscriptionRepository subscriptionRepository;

    public CreateSubscriptionUseCaseImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription createSubscription(User user, CreateSubscriptionRequest request) {
        Subscription subscription = new Subscription(user, request.getPlanName(), request.getPrice(), LocalDate.now(), LocalDate.now().plusMonths(1), true);
        return subscriptionRepository.save(subscription);
    }
}
