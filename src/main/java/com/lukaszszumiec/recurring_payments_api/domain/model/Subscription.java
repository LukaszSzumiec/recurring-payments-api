package com.lukaszszumiec.recurring_payments_api.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    private UUID id;


}
