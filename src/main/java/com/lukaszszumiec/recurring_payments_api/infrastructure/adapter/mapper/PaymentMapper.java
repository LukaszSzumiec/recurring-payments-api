package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.mapper;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    private final SubscriptionMapper subscriptionMapper;

    public PaymentMapper(SubscriptionMapper subscriptionMapper) {
        this.subscriptionMapper = subscriptionMapper;
    }

    public Payment toDomain(PaymentEntity e) {
        if (e == null)
            return null;
        return new Payment(
                e.getId(),
                subscriptionMapper.toDomain(e.getSubscription()),
                e.getAmount(),
                e.getStatus(),
                e.getCreatedAt());
    }

    public PaymentEntity toEntity(Payment d) {
        if (d == null)
            return null;
        return new PaymentEntity(
                d.getId(),
                subscriptionMapper.toEntity(d.getSubscription()),
                d.getAmount(),
                d.getStatus(),
                d.getCreatedAt());
    }
}
