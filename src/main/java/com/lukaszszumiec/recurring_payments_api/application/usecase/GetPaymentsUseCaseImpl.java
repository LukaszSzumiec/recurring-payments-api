package com.lukaszszumiec.recurring_payments_api.application.usecase;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.domain.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetPaymentsUseCaseImpl implements GetPaymentsUseCase{

    private final PaymentRepository paymentRepository;

    public GetPaymentsUseCaseImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> getPaymentsForUser(User user) {
        return user.getSubscriptions()
                .stream()
                .flatMap(subscription -> paymentRepository.findBySubscription(subscription).stream())
                .collect(Collectors.toList());
    }
}
