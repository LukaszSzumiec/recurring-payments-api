package com.lukaszszumiec.recurring_payments_api.infrastructure.scheduler;

import com.lukaszszumiec.recurring_payments_api.application.usecase.ProcessPaymentUseCase;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PaymentScheduler implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(PaymentScheduler.class);

    private final SubscriptionRepository subscriptionRepository;
    private final ProcessPaymentUseCase processPayment;
    private final String cron;

    // Wstrzykujemy CRON z domyślną wartością (co minutę), jeśli property nie istnieje
    public PaymentScheduler(SubscriptionRepository subscriptionRepository,
                            ProcessPaymentUseCase processPayment,
                            @Value("${billing.charge-cron:0 * * * * *}") String cron) {
        this.subscriptionRepository = subscriptionRepository;
        this.processPayment = processPayment;
        this.cron = cron;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // Rejestrujemy zadanie z CRON pobranym z properties (lub domyślnym)
        taskRegistrar.addCronTask(new CronTask(this::runCharge, cron));
        log.info("[scheduler] Registered cron task with expression: {}", cron);
    }

    // Właściwa logika – bez adnotacji @Scheduled
    private void runCharge() {
        var today = LocalDate.now();
        var due = subscriptionRepository.findAllDueUntil(today);

        if (due.isEmpty()) {
            log.info("[scheduler] No subscriptions due as of {}", today);
            return;
        }

        log.info("[scheduler] Processing {} due subscription(s) as of {}", due.size(), today);
        due.forEach(processPayment::processForSubscription);
    }
}
