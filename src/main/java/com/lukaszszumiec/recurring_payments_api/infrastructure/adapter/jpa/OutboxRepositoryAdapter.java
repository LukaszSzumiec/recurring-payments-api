package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.jpa;

import com.lukaszszumiec.recurring_payments_api.domain.model.OutboxEvent;
import com.lukaszszumiec.recurring_payments_api.domain.port.OutboxRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class OutboxRepositoryAdapter implements OutboxRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public OutboxEvent save(OutboxEvent event) {
        em.persist(event);
        return event;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutboxEvent> findPending(int limit) {
        return em.createQuery("select o from OutboxEvent o where o.status = 'PENDING' order by o.id",
                OutboxEvent.class)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public void markSent(Long id) {
        OutboxEvent e = em.find(OutboxEvent.class, id);
        if (e != null) {
            e.setStatus("SENT");
            e.setLastAttemptAt(LocalDateTime.now());
        }
    }

    @Override
    public void markFailed(Long id, int attempts) {
        OutboxEvent e = em.find(OutboxEvent.class, id);
        if (e != null) {
            e.setStatus("FAILED");
            e.setAttempts(attempts);
            e.setLastAttemptAt(LocalDateTime.now());
        }
    }
}
