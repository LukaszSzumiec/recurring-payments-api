package com.lukaszszumiec.recurring_payments_api.application.impl;

import com.lukaszszumiec.recurring_payments_api.application.PaymentQueryService;
import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.port.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final PaymentRepository paymentRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> getByUserId(Long userId) {
        return paymentRepository.findAllByUserId(userId);
    }
}
