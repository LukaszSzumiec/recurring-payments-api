package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.mapper.SubscriptionMapper;
import com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.repo.SpringDataSubscriptionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final SpringDataSubscriptionRepository jpa;
    private final SubscriptionMapper mapper;

    public SubscriptionRepositoryAdapter(SpringDataSubscriptionRepository jpa,
            SubscriptionMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Subscription save(Subscription s) {
        return mapper.toDomain(jpa.save(mapper.toEntity(s)));
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Subscription> findAllDueUntil(LocalDate date) {
        return jpa.findAllByNextChargeDateLessThanEqual(date)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
