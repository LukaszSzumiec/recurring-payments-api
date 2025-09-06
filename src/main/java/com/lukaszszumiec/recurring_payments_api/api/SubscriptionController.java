package com.lukaszszumiec.recurring_payments_api.api;

import com.lukaszszumiec.recurring_payments_api.api.mapper.SubscriptionMapper;
import com.lukaszszumiec.recurring_payments_api.application.PaymentProcessingService;
import com.lukaszszumiec.recurring_payments_api.application.SubscriptionService;
import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionCommand;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import com.lukaszszumiec.recurring_payments_api.generated.api.SubscriptionsApi;
import com.lukaszszumiec.recurring_payments_api.generated.model.CreateSubscriptionRequestDto;
import com.lukaszszumiec.recurring_payments_api.generated.model.SubscriptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class SubscriptionController implements SubscriptionsApi {

    private final SubscriptionService subscriptionService;
    private final PaymentProcessingService paymentProcessingService;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionController(SubscriptionService subscriptionService,
                                     PaymentProcessingService paymentProcessingService,
                                     SubscriptionRepository subscriptionRepository,
                                     SubscriptionMapper subscriptionMapper) {
        this.subscriptionService = subscriptionService;
        this.paymentProcessingService = paymentProcessingService;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionMapper = subscriptionMapper;
    }

    @Override
    public ResponseEntity<SubscriptionDto> createSubscription(CreateSubscriptionRequestDto body) {
        var created = subscriptionService.create(
                new CreateSubscriptionCommand(
                        body.getUserId(),
                        new BigDecimal(body.getPrice()),
                        body.getBillingDayOfMonth()
                )
        );
        return ResponseEntity.ok(subscriptionMapper.toDto(created));
    }

    @Override
    public ResponseEntity<Void> processSubscriptionNow(Long subscriptionId) {
        var sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + subscriptionId));
        paymentProcessingService.processForSubscription(sub);
        return ResponseEntity.accepted().build();
    }
}
