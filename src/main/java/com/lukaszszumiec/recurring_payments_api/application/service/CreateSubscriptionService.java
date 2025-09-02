package com.lukaszszumiec.recurring_payments_api.application.service;

import com.lukaszszumiec.recurring_payments_api.application.usecase.CreateSubscriptionUseCase;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import com.lukaszszumiec.recurring_payments_api.domain.port.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CreateSubscriptionService implements CreateSubscriptionUseCase {

    private final UserRepository userRepo;
    private final SubscriptionRepository subRepo;

    public CreateSubscriptionService(UserRepository userRepo,
            SubscriptionRepository subRepo) {
        this.userRepo = userRepo;
        this.subRepo = subRepo;
    }

    @Override
    public Subscription create(CreateSubscriptionCommand cmd) {
        User user = userRepo.findById(cmd.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + cmd.userId()));

        int day = cmd.billingDayOfMonth();
        LocalDate today = LocalDate.now();
        LocalDate next = today.withDayOfMonth(Math.min(day, today.lengthOfMonth()));

        Subscription s = new Subscription();
        s.setUser(user);
        s.setPrice(new java.math.BigDecimal(cmd.price()));
        s.setBillingDayOfMonth(day);
        s.setNextChargeDate(next);
        return subRepo.save(s);

    }
}
