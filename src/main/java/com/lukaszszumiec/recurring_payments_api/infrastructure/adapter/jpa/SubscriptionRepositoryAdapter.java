package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.jpa;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Subscription save(Subscription s) {
        if (s.getId() == null) {
            em.persist(s);
            return s;
        } else {
            return em.merge(s);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Subscription> findById(Long id) {
        return Optional.ofNullable(em.find(Subscription.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subscription> findAllDueUntil(LocalDate date) {
        return em.createQuery("""
                select s
                from Subscription s
                where s.nextChargeDate <= :date
                order by s.nextChargeDate asc
                """, Subscription.class)
                .setParameter("date", date)
                .getResultList();
    }
}
