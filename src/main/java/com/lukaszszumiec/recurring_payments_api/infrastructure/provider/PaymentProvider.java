package com.lukaszszumiec.recurring_payments_api.infrastructure.provider;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import java.math.BigDecimal;

public sealed interface PaymentProvider permits FakePaymentProvider {
    boolean charge(Subscription subscription, BigDecimal amount);
}
