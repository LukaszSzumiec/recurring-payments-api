package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepositoryAdapter
        extends JpaRepository<Subscription, Long>, SubscriptionRepository {

    List<Subscription> findByNextChargeDateLessThanEqualOrderByNextChargeDateAsc(LocalDate date);

    @Override
    default List<Subscription> findAllDueUntil(LocalDate date) {
        return findByNextChargeDateLessThanEqualOrderByNextChargeDateAsc(date);
    }

}
