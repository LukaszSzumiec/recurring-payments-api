package com.lukaszszumiec.recurring_payments_api.application.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

@Service
public class PaymentProcessingServiceImpl implements PaymentProcessingService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentProvider paymentProvider;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public PaymentProcessingServiceImpl(PaymentRepository paymentRepository,
                                        SubscriptionRepository subscriptionRepository,
                                        PaymentProvider paymentProvider,
                                        OutboxRepository outboxRepository,
                                        ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentProvider = paymentProvider;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
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

        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(Map.of(
                    "type", "PaymentCharged",
                    "version", 1,
                    "paymentId", payment.getId(),
                    "subscriptionId", subscription.getId(),
                    "status", payment.getStatus().name(),
                    "amount", payment.getAmount().toString(),
                    "createdAt", payment.getCreatedAt().toString()
            ));
        } catch (Exception e) {
            payloadJson = "{\"type\":\"PaymentCharged\",\"paymentId\":" + payment.getId() + ",\"status\":\"" + payment.getStatus() + "\"}";
        }

        outboxRepository.store(
                OutboxEvent.builder()
                        .aggregateType("Payment")
                        .aggregateId(payment.getId())
                        .payload(payloadJson)
                        .status("PENDING")
                        .build());
    }
}
