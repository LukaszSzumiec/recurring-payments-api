package com.lukaszszumiec.recurring_payments_api.application.dto;

import java.math.BigDecimal;

public record CreateSubscriptionCommand(Long userId, BigDecimal price, int billingDayOfMonth) {}
