package com.lukaszszumiec.recurring_payments_api.infrastructure.scheduler;

import com.lukaszszumiec.recurring_payments_api.domain.model.OutboxEvent;
import com.lukaszszumiec.recurring_payments_api.domain.port.OutboxRepository;
import com.lukaszszumiec.recurring_payments_api.infrastructure.publisher.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPublisher implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutboxRepository outbox;
    private final EventPublisher publisher;
    private final String cron;
    private final int batchSize;

    public OutboxPublisher(OutboxRepository outbox,
                           EventPublisher publisher,
                           @Value("${outbox.publish-cron:*/15 * * * * *}") String cron,
                           @Value("${outbox.batch-size:100}") int batchSize) {
        this.outbox = outbox;
        this.publisher = publisher;
        this.cron = cron;
        this.batchSize = batchSize;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addCronTask(new CronTask(this::publishBatch, cron));
        log.info("[outbox] Registered publisher cron={}, batchSize={}", cron, batchSize);
    }

    private void publishBatch() {
        List<OutboxEvent> events = outbox.findPending(batchSize);
        if (events.isEmpty()) return;

        for (OutboxEvent ev : events) {
            try {
                publisher.publish(ev.getAggregateType(), ev.getAggregateId(), ev.getPayload());
                outbox.markSent(ev.getId());
            } catch (Exception ex) {
                outbox.markFailed(ev.getId(), ev.getAttempts() + 1);
                log.warn("[outbox] publish failed id={} attempts={}", ev.getId(), ev.getAttempts() + 1, ex);
            }
        }
    }
}
