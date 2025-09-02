package com.lukaszszumiec.recurring_payments_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private Subscription subscription;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;

    public Payment() {}
    public Payment(Long id, Subscription subscription, BigDecimal amount, PaymentStatus status, LocalDateTime createdAt) {
        this.id = id; this.subscription = subscription; this.amount = amount; this.status = status; this.createdAt = createdAt;
    }

    public Long getId() { return id; }                         public void setId(Long id) { this.id = id; }
    public Subscription getSubscription() { return subscription; } public void setSubscription(Subscription subscription) { this.subscription = subscription; }
    public BigDecimal getAmount() { return amount; }           public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentStatus getStatus() { return status; }        public void setStatus(PaymentStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
