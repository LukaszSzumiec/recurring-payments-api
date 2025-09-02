package com.lukaszszumiec.recurring_payments_api.application.service;

import com.lukaszszumiec.recurring_payments_api.application.usecase.GetPaymentsUseCase;
import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.port.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPaymentsService implements GetPaymentsUseCase {

    private final PaymentRepository paymentRepository;

    public GetPaymentsService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> getByUserId(Long userId) {
        return paymentRepository.findAllByUserId(userId);
    }
}
