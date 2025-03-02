package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter;

import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository <User, UUID>, UserRepository {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
