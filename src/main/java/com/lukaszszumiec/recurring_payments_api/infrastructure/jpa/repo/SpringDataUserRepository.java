package com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.repo;

import com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
}
