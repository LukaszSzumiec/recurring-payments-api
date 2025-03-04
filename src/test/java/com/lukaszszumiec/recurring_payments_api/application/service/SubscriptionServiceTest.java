package com.lukaszszumiec.recurring_payments_api.application.service;

import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionRequest;
import com.lukaszszumiec.recurring_payments_api.application.dto.SubscriptionResponse;
import com.lukaszszumiec.recurring_payments_api.application.usecase.CreateSubscriptionUseCase;
import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class SubscriptionServiceTest {

    private SubscriptionService subscriptionService;
    private CreateSubscriptionUseCase createSubscriptionUseCase;

    @BeforeEach
    void setUp(){
        createSubscriptionUseCase = mock(CreateSubscriptionUseCase.class);
        subscriptionService = new SubscriptionService(
                createSubscriptionUseCase
        );
    }

    @Test
    void createSubscription_ShouldReturnCreatedSubscription(){
        User user = new User("testUser", "test@example.com", "pass", "USER");
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setPlanName("Premium");
        request.setPrice(10.99);

        Subscription expectedSubscription = new Subscription(user, "Premium", 10.99, null, null, true);
        when(createSubscriptionUseCase.createSubscription(user, request)).thenReturn(expectedSubscription);

        Subscription result = subscriptionService.createSubscription(user, request);
        assertNotNull(result);
        assertEquals("Premium", result.getPlanName());
        assertEquals(10.99, result.getPrice());
        verify(createSubscriptionUseCase, times(1)).createSubscription(user, request);
    }
}

//@SpringBootTest
//class RecurringPaymentsApiApplicationTests {
//
//    @Test
//    void contextLoads() {
//    }
//
//}
