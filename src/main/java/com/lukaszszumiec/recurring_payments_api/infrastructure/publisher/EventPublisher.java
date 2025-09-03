package com.lukaszszumiec.recurring_payments_api.infrastructure.publisher;

public interface EventPublisher {
    void publish(String aggregateType, long aggregateId, String payloadJson);
}
