package com.lukaszszumiec.recurring_payments_api.infrastructure.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogEventPublisher implements EventPublisher {
    private static final Logger log = LoggerFactory.getLogger(LogEventPublisher.class);

    @Override
    public void publish(String aggregateType, long aggregateId, String payloadJson) {
        log.info("[event] {}#{} -> {}", aggregateType, aggregateId, payloadJson);
    }
}
