package com.lukaszszumiec.recurring_payments_api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CreateSubscriptionRequest {

    @NotBlank
    private String planName;

    @Positive
    private double price;

    public String getPlanName(){
        return planName;
    }

    public double getPrice(){
        return price;
    }

    public void setPlanName(String planName){
        this.planName = planName;
    }

    public void setPrice(double price){
        this.price = price;
    }
}
