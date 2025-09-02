package com.lukaszszumiec.recurring_payments_api.infrastructure.provider;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component
public non-sealed class FakePaymentProvider implements PaymentProvider {
    @Override
    public boolean charge(Subscription subscription, BigDecimal amount) {
        return ThreadLocalRandom.current().nextInt(10) != 0;
    }
}
