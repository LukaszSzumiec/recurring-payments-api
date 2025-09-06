package com.lukaszszumiec.recurring_payments_api.domain.port;

import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
}
