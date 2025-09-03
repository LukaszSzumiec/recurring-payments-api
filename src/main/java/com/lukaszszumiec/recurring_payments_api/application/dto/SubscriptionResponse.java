package com.lukaszszumiec.recurring_payments_api.application.dto;

import java.time.LocalDate;
import java.util.UUID;

public class SubscriptionResponse {
    private UUID id;
    private String planName;
    private double price; 
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    public SubscriptionResponse(UUID id, String planName, double price, LocalDate startDate, LocalDate endDate, boolean active) {
        this.id = id;
        this.planName = planName;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public double getPrice() {
        return price;
    }
}
