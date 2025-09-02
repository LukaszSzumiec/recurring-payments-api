package com.lukaszszumiec.recurring_payments_api.application.service;

import com.lukaszszumiec.recurring_payments_api.application.usecase.ProcessPaymentUseCase;
import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.model.PaymentStatus;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.port.PaymentRepository;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import com.lukaszszumiec.recurring_payments_api.infrastructure.provider.PaymentProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ProcessPaymentService implements ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentProvider paymentProvider;

    public ProcessPaymentService(PaymentRepository paymentRepository,
            SubscriptionRepository subscriptionRepository,
            PaymentProvider paymentProvider) {
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentProvider = paymentProvider;
    }

    @Override
    @Transactional
    public void processForSubscription(Subscription subscription) {
        Payment payment = new Payment();
        payment.setSubscription(subscription);
        payment.setAmount(subscription.getPrice());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(java.time.LocalDateTime.now());
        payment = paymentRepository.save(payment);

        boolean ok = paymentProvider.charge(subscription, payment.getAmount());
        payment.setStatus(ok ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        paymentRepository.save(payment);

        if (ok) {
            java.time.LocalDate next = subscription.getNextChargeDate().plusMonths(1);
            int day = subscription.getBillingDayOfMonth();
            subscription.setNextChargeDate(next.withDayOfMonth(Math.min(day, next.lengthOfMonth())));
            subscriptionRepository.save(subscription);
        }
    }
}
