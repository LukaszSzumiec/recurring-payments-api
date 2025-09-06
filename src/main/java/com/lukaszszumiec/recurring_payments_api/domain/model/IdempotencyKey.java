package com.lukaszszumiec.recurring_payments_api.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idem_key", nullable = false, unique = true, length = 128)
    private String key;

    @Column(nullable = false, length = 10)
    private String method;

    @Column(nullable = false, length = 255)
    private String path;

    @Column(name = "fingerprint", nullable = false, length = 128)
    private String fingerprint;

    @Column(name = "response_status", nullable = false)
    private int responseStatus;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(name = "response_body", columnDefinition = "text")
    private String responseBody;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
