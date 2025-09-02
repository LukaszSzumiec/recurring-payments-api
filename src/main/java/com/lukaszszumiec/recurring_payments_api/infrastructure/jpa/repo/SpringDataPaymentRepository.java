package com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.repo;

import com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findAllBySubscription_User_Id(Long userId);
}
