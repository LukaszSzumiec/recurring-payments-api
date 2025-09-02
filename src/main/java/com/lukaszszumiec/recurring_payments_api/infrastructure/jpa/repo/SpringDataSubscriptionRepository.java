package com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.repo;

import com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface SpringDataSubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findAllByNextChargeDateLessThanEqual(LocalDate date);
}
