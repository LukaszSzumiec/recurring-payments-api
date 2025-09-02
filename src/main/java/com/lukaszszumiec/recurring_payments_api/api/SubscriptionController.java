package com.lukaszszumiec.recurring_payments_api.api;

import com.lukaszszumiec.recurring_payments_api.api.dto.CreateSubscriptionRequest;
import com.lukaszszumiec.recurring_payments_api.application.usecase.CreateSubscriptionUseCase;
import com.lukaszszumiec.recurring_payments_api.application.usecase.GetPaymentsUseCase;
import com.lukaszszumiec.recurring_payments_api.application.usecase.ProcessPaymentUseCase;
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

    private final CreateSubscriptionUseCase createSubscription;
    private final ProcessPaymentUseCase processPayment;
    private final GetPaymentsUseCase getPayments;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionController(CreateSubscriptionUseCase createSubscription,
            ProcessPaymentUseCase processPayment,
            GetPaymentsUseCase getPayments,
            SubscriptionRepository subscriptionRepository) {
        this.createSubscription = createSubscription;
        this.processPayment = processPayment;
        this.getPayments = getPayments;
        this.subscriptionRepository = subscriptionRepository;
    }

    @PostMapping
    public ResponseEntity<Subscription> create(@Valid @RequestBody CreateSubscriptionRequest req) {
        var created = createSubscription.create(
                new CreateSubscriptionUseCase.CreateSubscriptionCommand(
                        req.userId(),
                        req.price(),
                        req.billingDayOfMonth()));
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<Void> processNow(@PathVariable Long id) {
        var s = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + id));
        processPayment.processForSubscription(s);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/user/{userId}/payments")
    public ResponseEntity<List<Payment>> listUserPayments(@PathVariable Long userId) {
        var payments = getPayments.getByUserId(userId);
        return ResponseEntity.ok(payments);
    }
}
