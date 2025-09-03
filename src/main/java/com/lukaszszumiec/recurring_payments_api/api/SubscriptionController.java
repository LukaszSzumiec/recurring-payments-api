package com.lukaszszumiec.recurring_payments_api.api;

import com.lukaszszumiec.recurring_payments_api.api.dto.CreateSubscriptionRequest;
import com.lukaszszumiec.recurring_payments_api.application.PaymentProcessingService;
import com.lukaszszumiec.recurring_payments_api.application.PaymentQueryService;
import com.lukaszszumiec.recurring_payments_api.application.SubscriptionService;
import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionCommand;
import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final PaymentProcessingService paymentProcessingService;
    private final PaymentQueryService paymentQueryService;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionController(SubscriptionService subscriptionService,
                                  PaymentProcessingService paymentProcessingService,
                                  PaymentQueryService paymentQueryService,
                                  SubscriptionRepository subscriptionRepository) {
        this.subscriptionService = subscriptionService;
        this.paymentProcessingService = paymentProcessingService;
        this.paymentQueryService = paymentQueryService;
        this.subscriptionRepository = subscriptionRepository;
    }

    @PostMapping
    public ResponseEntity<Subscription> create(@Valid @RequestBody CreateSubscriptionRequest req) {
        var created = subscriptionService.create(
                new CreateSubscriptionCommand(
                        req.userId(),
                        req.price(),
                        req.billingDayOfMonth()
                )
        );
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<Void> processNow(@PathVariable Long id) {
        var s = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + id));
        paymentProcessingService.processForSubscription(s);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/user/{userId}/payments")
    public ResponseEntity<List<Payment>> listUserPayments(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentQueryService.getByUserId(userId));
    }
}
