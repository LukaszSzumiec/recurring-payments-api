package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.jpa;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.port.PaymentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class PaymentRepositoryAdapter implements PaymentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Payment save(Payment p) {
        if (p.getId() == null) {
            em.persist(p);
            return p;
        } else {
            return em.merge(p);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findAllByUserId(Long userId) {
        return em.createQuery("""
                select p
                from Payment p
                join p.subscription s
                join s.user u
                where u.id = :userId
                order by p.createdAt desc
                """, Payment.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
