package com.lukaszszumiec.recurring_payments_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Subscription {
    private Long id;
    private User user;
    private BigDecimal price;
    private LocalDate nextChargeDate;
    private int billingDayOfMonth;

    public Subscription() {}
    public Subscription(Long id, User user, BigDecimal price, LocalDate nextChargeDate, int billingDayOfMonth) {
        this.id = id; this.user = user; this.price = price; this.nextChargeDate = nextChargeDate; this.billingDayOfMonth = billingDayOfMonth;
    }

    public Long getId() { return id; }                           public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }                       public void setUser(User user) { this.user = user; }
    public BigDecimal getPrice() { return price; }               public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDate getNextChargeDate() { return nextChargeDate; } public void setNextChargeDate(LocalDate nextChargeDate) { this.nextChargeDate = nextChargeDate; }
    public int getBillingDayOfMonth() { return billingDayOfMonth; } public void setBillingDayOfMonth(int billingDayOfMonth) { this.billingDayOfMonth = billingDayOfMonth; }
}
