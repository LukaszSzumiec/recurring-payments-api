package com.lukaszszumiec.recurring_payments_api.domain.model;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String planName;

    private int price;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean active;

    public Subscription() {
    }

    public Subscription(User user, String planName, double price, LocalDate startDate, LocalDate endDate, boolean active) {
        this.user = user;
        this.planName = planName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }
}
