package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.port.PaymentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepositoryAdapter
        extends JpaRepository<Payment, Long>, PaymentRepository {

    List<Payment> findAllBySubscription_User_IdOrderByCreatedAtDesc(Long userId);

    @Override
    default List<Payment> findAllByUserId(Long userId) {
        return findAllBySubscription_User_IdOrderByCreatedAtDesc(userId);
    }

}
