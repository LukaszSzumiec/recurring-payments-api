package com.lukaszszumiec.recurring_payments_api.application.usecase;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.model.PaymentStatus;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.repository.PaymentRepository;

import java.time.LocalDate;

public class ProcessPaymentUseCaseImpl implements ProcessPaymentUseCase{

    private final PaymentRepository paymentRepository;

    public ProcessPaymentUseCaseImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void processPaymentForSubscription(Subscription subscription) {
        Payment payment = new Payment(
                subscription,
                subscription.getPrice(),
                LocalDate.now(),
                PaymentStatus.PENDING
        );
        paymentRepository.save(payment);
    }
}
