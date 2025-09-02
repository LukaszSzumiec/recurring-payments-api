package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter;

import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.domain.port.UserRepository;
import com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.mapper.UserMapper;
import com.lukaszszumiec.recurring_payments_api.infrastructure.jpa.repo.SpringDataUserRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepository {
    private final SpringDataUserRepository jpa;
    private final UserMapper mapper;

    public UserRepositoryAdapter(SpringDataUserRepository jpa, UserMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        return mapper.toDomain(jpa.save(mapper.toEntity(user)));
    }
}
