package com.lukaszszumiec.recurring_payments_api.application.usecase;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import java.util.List;


public interface GetSubscriptionsUseCase {
    List<Subscription> getActiveSubscriptions(User user);
}
