package com.lukaszszumiec.recurring_payments_api.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

public class SubscriptionResponse {
    @Setter
    @Getter
    private UUID id;
    @Setter
    private String planName;
    @Getter
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

    public String getPlanName() {
        return planName;
    }

}
