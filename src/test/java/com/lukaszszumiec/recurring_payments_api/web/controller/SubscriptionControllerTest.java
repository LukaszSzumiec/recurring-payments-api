package com.lukaszszumiec.recurring_payments_api.web.controller;

import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionRequest;
import com.lukaszszumiec.recurring_payments_api.application.service.SubscriptionService;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubscriptionControllerTest {

    private MockMvc mockMvc;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        subscriptionService = mock(SubscriptionService.class);
        SubscriptionController subscriptionController = new SubscriptionController(subscriptionService);
        mockMvc = MockMvcBuilders.standaloneSetup(subscriptionController).build();
    }

    @Test
    void createSubscription_ShouldReturnCreatedSubscription() throws Exception {
        User user = new User("testUser", "test@example.com", "password", "USER");
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setPlanName("Premium");
        request.setPrice(19.99);

        Subscription subscription = new Subscription(user, "Premium", 19.99, null, null, true);
        when(subscriptionService.createSubscription(any(User.class), any(CreateSubscriptionRequest.class))).thenReturn(subscription);

        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"planName\": \"Premium\", \"price\": 19.99}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName").value("Premium"))
                .andExpect(jsonPath("$.price").value(19.99));

        verify(subscriptionService, times(1)).createSubscription(any(User.class), any(CreateSubscriptionRequest.class));
    }
}
