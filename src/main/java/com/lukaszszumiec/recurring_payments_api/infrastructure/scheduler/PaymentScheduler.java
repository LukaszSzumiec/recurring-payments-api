package com.lukaszszumiec.recurring_payments_api.infrastructure.scheduler;

import com.lukaszszumiec.recurring_payments_api.application.PaymentProcessingService;
import com.lukaszszumiec.recurring_payments_api.config.BillingProperties;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PaymentScheduler implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(PaymentScheduler.class);

    private final SubscriptionRepository subscriptionRepository;
    private final PaymentProcessingService paymentProcessingService;
    private final BillingProperties billingProperties;

    public PaymentScheduler(SubscriptionRepository subscriptionRepository,
                            PaymentProcessingService paymentProcessingService,
                            BillingProperties billingProperties) {
        this.subscriptionRepository = subscriptionRepository;
        this.paymentProcessingService = paymentProcessingService;
        this.billingProperties = billingProperties;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addCronTask(new CronTask(this::runCharge, billingProperties.chargeCron()));
        log.info("[scheduler] Registered cron task with expression: {}", billingProperties.chargeCron());
    }

    private void runCharge() {
        var today = LocalDate.now();
        var due = subscriptionRepository.findAllDueUntil(today);

        if (due.isEmpty()) {
            log.info("[scheduler] No subscriptions due as of {}", today);
            return;
        }

        log.info("[scheduler] Processing {} due subscription(s) as of {}", due.size(), today);
        due.forEach(paymentProcessingService::processForSubscription);
    }
}
