package com.lukaszszumiec.recurring_payments_api.application.dto;

public record CreateSubscriptionCommand(Long userId, double price, int billingDayOfMonth) {}
