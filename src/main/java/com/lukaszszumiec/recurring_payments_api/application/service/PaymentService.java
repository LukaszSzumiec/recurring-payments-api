package com.lukaszszumiec.recurring_payments_api.application.service;

import com.lukaszszumiec.recurring_payments_api.application.usecase.GetPaymentsUseCase;
import com.lukaszszumiec.recurring_payments_api.application.usecase.ProcessPaymentUseCase;
import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final GetPaymentsUseCase getPaymentsUseCase;
    private final ProcessPaymentUseCase processPaymentUseCase;

    public PaymentService(GetPaymentsUseCase getPaymentsUseCase, ProcessPaymentUseCase processPaymentUseCase) {
        this.getPaymentsUseCase = getPaymentsUseCase;
        this.processPaymentUseCase = processPaymentUseCase;
    }

    public List<Payment> getUserPayments(User user){
        return getPaymentsUseCase.getPaymentsForUser(user);
    }

    public void processPayment(Subscription subscription){
        processPaymentUseCase.processPaymentForSubscription(subscription);
    }
}
