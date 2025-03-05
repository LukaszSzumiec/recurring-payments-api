package com.lukaszszumiec.recurring_payments_api.domain.repository;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends BaseRepository<Payment, UUID> {
    List<Payment> findBySubscription(Subscription subscription);
}
