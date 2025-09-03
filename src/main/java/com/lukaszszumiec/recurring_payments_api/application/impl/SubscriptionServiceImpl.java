package com.lukaszszumiec.recurring_payments_api.application.impl;

import com.lukaszszumiec.recurring_payments_api.application.SubscriptionService;
import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionCommand;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import com.lukaszszumiec.recurring_payments_api.domain.port.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepo;
    private final SubscriptionRepository subRepo;

    public SubscriptionServiceImpl(UserRepository userRepo, SubscriptionRepository subRepo) {
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

        Subscription s = Subscription.builder()
                .user(user)
                .price(BigDecimal.valueOf(cmd.price()))
                .billingDayOfMonth(day)
                .nextChargeDate(next)
                .build();

        return subRepo.save(s);
    }
}
