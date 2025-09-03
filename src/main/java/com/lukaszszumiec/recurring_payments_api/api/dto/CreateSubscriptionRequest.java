package com.lukaszszumiec.recurring_payments_api.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateSubscriptionRequest(
        @NotNull Long userId,
        @NotNull @DecimalMin("0.00") BigDecimal price,
        @Min(1) @Max(28) Integer billingDayOfMonth
) { }
