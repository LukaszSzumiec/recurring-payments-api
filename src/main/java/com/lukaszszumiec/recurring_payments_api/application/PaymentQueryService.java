package com.lukaszszumiec.recurring_payments_api.application;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentQueryService {
    List<Payment> getByUserId(Long userId);
    Page<Payment> getByUserId(Long userId, Pageable pageable);
}
