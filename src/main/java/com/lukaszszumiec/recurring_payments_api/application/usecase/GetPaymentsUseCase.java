package com.lukaszszumiec.recurring_payments_api.application.usecase;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;

import java.util.List;

public interface GetPaymentsUseCase {
    List<Payment> getPaymentsForUser(User user);
}
