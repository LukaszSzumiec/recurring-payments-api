package com.lukaszszumiec.recurring_payments_api.infrastructure.adapter;

import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.testutil.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryAdapterIT extends AbstractIntegrationTest {

    @Autowired
    UserRepositoryAdapter users;

    @Test
    void findByEmail_shouldReturnSavedUser() {
        var saved = users.save(User.builder()
                .email("john@doe.com")
                .fullName("John Doe")
                .password("{noop}pass")
                .build());

        var found = users.findByEmail("john@doe.com");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
    }
}
