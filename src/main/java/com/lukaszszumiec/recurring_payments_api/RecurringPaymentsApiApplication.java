package com.lukaszszumiec.recurring_payments_api;

import com.lukaszszumiec.recurring_payments_api.config.BillingProperties;
import com.lukaszszumiec.recurring_payments_api.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({JwtProperties.class, BillingProperties.class})
public class RecurringPaymentsApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecurringPaymentsApiApplication.class, args);
    }
}
