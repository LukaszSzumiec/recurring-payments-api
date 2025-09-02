package com.lukaszszumiec.recurring_payments_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "billing")
public record BillingProperties(String chargeCron) { }
