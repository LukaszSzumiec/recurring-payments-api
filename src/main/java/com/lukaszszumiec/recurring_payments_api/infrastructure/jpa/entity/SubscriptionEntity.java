package com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "next_charge_date", nullable = false)
    private LocalDate nextChargeDate;

    @Column(name = "billing_day_of_month", nullable = false)
    private int billingDayOfMonth;

    public SubscriptionEntity() {
    }

    public SubscriptionEntity(Long id, UserEntity user, BigDecimal price, LocalDate nextChargeDate,
            int billingDayOfMonth) {
        this.id = id;
        this.user = user;
        this.price = price;
        this.nextChargeDate = nextChargeDate;
        this.billingDayOfMonth = billingDayOfMonth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getNextChargeDate() {
        return nextChargeDate;
    }

    public void setNextChargeDate(LocalDate nextChargeDate) {
        this.nextChargeDate = nextChargeDate;
    }

    public int getBillingDayOfMonth() {
        return billingDayOfMonth;
    }

    public void setBillingDayOfMonth(int billingDayOfMonth) {
        this.billingDayOfMonth = billingDayOfMonth;
    }
}
