package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.mapper;

import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserEntity e) {
        if (e == null)
            return null;
        return new User(e.getId(), e.getEmail(), e.getFullName());
    }

    public UserEntity toEntity(User d) {
        if (d == null)
            return null;
        return new UserEntity(d.getId(), d.getEmail(), d.getFullName());
    }
}
