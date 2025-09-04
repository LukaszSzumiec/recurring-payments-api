package com.lukaszszumiec.recurring_payments_api.application.impl;

import com.lukaszszumiec.recurring_payments_api.application.PaymentProcessingService;
import com.lukaszszumiec.recurring_payments_api.domain.model.OutboxEvent;
import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.model.PaymentStatus;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.port.OutboxRepository;
import com.lukaszszumiec.recurring_payments_api.domain.port.PaymentRepository;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import com.lukaszszumiec.recurring_payments_api.infrastructure.provider.PaymentProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentProcessingServiceImpl implements PaymentProcessingService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentProvider paymentProvider;
    private final OutboxRepository outboxRepository;

    public PaymentProcessingServiceImpl(PaymentRepository paymentRepository,
            SubscriptionRepository subscriptionRepository,
            PaymentProvider paymentProvider,
            OutboxRepository outboxRepository) {
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentProvider = paymentProvider;
        this.outboxRepository = outboxRepository;
    }

    @Override
    @Transactional
    public void processForSubscription(Subscription subscription) {
        Payment payment = Payment.builder()
                .subscription(subscription)
                .amount(subscription.getPrice())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        boolean ok = paymentProvider.charge(subscription, payment.getAmount());
        payment.setStatus(ok ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        paymentRepository.save(payment);

        if (ok) {
            LocalDate next = subscription.getNextChargeDate().plusMonths(1);
            int day = subscription.getBillingDayOfMonth();
            subscription.setNextChargeDate(next.withDayOfMonth(Math.min(day, next.lengthOfMonth())));
            subscriptionRepository.save(subscription);
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("paymentId", payment.getId());
        payload.put("subscriptionId", subscription.getId());
        payload.put("status", payment.getStatus().name());
        payload.put("amount", payment.getAmount().toString());
        outboxRepository.store(
                OutboxEvent.builder()
                        .aggregateType("Payment")
                        .aggregateId(payment.getId())
                        .payload("{\"paymentId\":" + payment.getId() + ",\"status\":\"" + payment.getStatus() + "\"}")
                        .status("PENDING")
                        .build());

    }
}
