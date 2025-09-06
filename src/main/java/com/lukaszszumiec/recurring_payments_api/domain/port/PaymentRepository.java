package com.lukaszszumiec.recurring_payments_api.domain.port;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentRepository {
    Payment save(Payment p);
    List<Payment> findAllByUserId(Long userId);
    Page<Payment> findAllByUserId(Long userId, Pageable pageable);
}
