package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.mapper;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.entity.SubscriptionEntity;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    private final UserMapper userMapper;

    public SubscriptionMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Subscription toDomain(SubscriptionEntity e) {
        if (e == null)
            return null;
        return new Subscription(
                e.getId(),
                userMapper.toDomain(e.getUser()),
                e.getPrice(),
                e.getNextChargeDate(),
                e.getBillingDayOfMonth());
    }

    public SubscriptionEntity toEntity(Subscription d) {
        if (d == null)
            return null;
        return new SubscriptionEntity(
                d.getId(),
                userMapper.toEntity(d.getUser()),
                d.getPrice(),
                d.getNextChargeDate(),
                d.getBillingDayOfMonth());
    }
}
