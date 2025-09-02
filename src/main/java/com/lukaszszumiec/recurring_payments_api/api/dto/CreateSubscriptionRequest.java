package com.lukaszszumiec.recurring_payments_api.api.dto;

import jakarta.validation.constraints.*;

public record CreateSubscriptionRequest(
        @NotNull Long userId,
        @NotBlank String price,
        @Min(1) @Max(28) Integer billingDayOfMonth
) { }
