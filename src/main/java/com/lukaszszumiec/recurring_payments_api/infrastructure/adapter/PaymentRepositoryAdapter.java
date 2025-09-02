package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.port.PaymentRepository;
import com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.mapper.PaymentMapper;
import com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.repo.SpringDataPaymentRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final SpringDataPaymentRepository jpa;
    private final PaymentMapper mapper;

    public PaymentRepositoryAdapter(SpringDataPaymentRepository jpa, PaymentMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment p) {
        return mapper.toDomain(jpa.save(mapper.toEntity(p)));
    }

    @Override
    public List<Payment> findAllByUserId(Long userId) {
        return jpa.findAllBySubscription_User_Id(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
