package com.lukaszszumiec.recurring_payments_api.domain.port;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import java.util.List;

public interface PaymentRepository {
    Payment save(Payment p);

    List<Payment> findAllByUserId(Long userId);
}
