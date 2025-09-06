package com.lukaszszumiec.recurring_payments_api.application.impl;

import com.lukaszszumiec.recurring_payments_api.application.SubscriptionService;
import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionCommand;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import com.lukaszszumiec.recurring_payments_api.domain.port.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

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
        LocalDate target = YearMonth.from(today).atEndOfMonth().withDayOfMonth(Math.min(day, today.lengthOfMonth()));
        if (target.isBefore(today)) {
            YearMonth nextYm = YearMonth.from(today).plusMonths(1);
            target = nextYm.atEndOfMonth().withDayOfMonth(Math.min(day, nextYm.lengthOfMonth()));
        }

        Subscription s = Subscription.builder()
                .user(user)
                .price(cmd.price())
                .billingDayOfMonth(day)
                .nextChargeDate(target)
                .build();

        return subRepo.save(s);
    }
}
