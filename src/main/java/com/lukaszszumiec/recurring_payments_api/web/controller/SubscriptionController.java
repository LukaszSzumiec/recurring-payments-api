package com.lukaszszumiec.recurring_payments_api.web.controller;

import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionRequest;
import com.lukaszszumiec.recurring_payments_api.application.service.SubscriptionService;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getSubscriptions(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(subscriptionService.getSubscriptions(user));
    }

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@AuthenticationPrincipal User user, @Valid @RequestBody CreateSubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.createSubscription(user, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelSubscription(@AuthenticationPrincipal User user, @PathVariable UUID id){
        subscriptionService.cancelSubscription(id, user);
        return ResponseEntity.noContent().build();
    }

}
