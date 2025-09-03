package com.lukaszszumiec.recurring_payments_api.application;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;

import java.util.List;

public interface PaymentQueryService {
    List<Payment> getByUserId(Long userId);
}
