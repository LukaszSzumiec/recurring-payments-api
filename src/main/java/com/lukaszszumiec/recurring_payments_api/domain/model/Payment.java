package com.lukaszszumiec.recurring_payments_api.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.security.PublicKey;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @NotNull
    private double amount;

    @NotNull
    private LocalDate paymentDate;

    @Enumerated
    private PaymentStatus status;

    public Payment() {
    }

    public Payment(Subscription subscription, double amount, LocalDate paymentDate, PaymentStatus status) {
        this.subscription = subscription;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }


    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
