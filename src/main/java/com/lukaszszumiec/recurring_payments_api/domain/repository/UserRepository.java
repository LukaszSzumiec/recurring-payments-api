package com.lukaszszumiec.recurring_payments_api.domain.repository;

import com.lukaszszumiec.recurring_payments_api.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User save(User user);

    void deleteById(UUID id);

    boolean existsByEmail(String email);
}
