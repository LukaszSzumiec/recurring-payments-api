package com.lukaszszumiec.recurring_payments_api.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszszumiec.recurring_payments_api.api.mapper.SubscriptionMapper;
import com.lukaszszumiec.recurring_payments_api.application.PaymentProcessingService;
import com.lukaszszumiec.recurring_payments_api.application.SubscriptionService;
import com.lukaszszumiec.recurring_payments_api.application.dto.CreateSubscriptionCommand;
import com.lukaszszumiec.recurring_payments_api.domain.model.IdempotencyKey;
import com.lukaszszumiec.recurring_payments_api.domain.port.IdempotencyRepository;
import com.lukaszszumiec.recurring_payments_api.domain.port.SubscriptionRepository;
import com.lukaszszumiec.recurring_payments_api.generated.api.SubscriptionsApi;
import com.lukaszszumiec.recurring_payments_api.generated.model.CreateSubscriptionRequestDto;
import com.lukaszszumiec.recurring_payments_api.generated.model.SubscriptionDto;
import com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.UserRepositoryAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class SubscriptionController implements SubscriptionsApi {

    private final SubscriptionService subscriptionService;
    private final PaymentProcessingService paymentProcessingService;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepositoryAdapter userRepo;
    private final IdempotencyRepository idempotencyRepo;
    private final ObjectMapper objectMapper;

    public SubscriptionController(SubscriptionService subscriptionService,
                                  PaymentProcessingService paymentProcessingService,
                                  SubscriptionRepository subscriptionRepository,
                                  SubscriptionMapper subscriptionMapper,
                                  UserRepositoryAdapter userRepo,
                                  IdempotencyRepository idempotencyRepo,
                                  ObjectMapper objectMapper) {
        this.subscriptionService = subscriptionService;
        this.paymentProcessingService = paymentProcessingService;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionMapper = subscriptionMapper;
        this.userRepo = userRepo;
        this.idempotencyRepo = idempotencyRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<List<SubscriptionDto>> listSubscriptions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        List<com.lukaszszumiec.recurring_payments_api.domain.model.Subscription> subs;
        if (isAdmin) {
            subs = subscriptionRepository instanceof org.springframework.data.jpa.repository.JpaRepository<?,?> jpa
                    ? ((org.springframework.data.jpa.repository.JpaRepository<com.lukaszszumiec.recurring_payments_api.domain.model.Subscription, Long>) jpa).findAll()
                    : subscriptionRepository.findAllByUserId(-1L);
        } else {
            var me = userRepo.findByEmail(auth.getName()).map(u -> u.getId()).orElse(null);
            if (me == null) return ResponseEntity.status(401).build();
            subs = subscriptionRepository.findAllByUserId(me);
        }
        var list = subs.stream().map(subscriptionMapper::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<SubscriptionDto> getSubscription(Long subscriptionId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        var sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + subscriptionId));
        if (!isAdmin) {
            var me = userRepo.findByEmail(auth.getName()).map(u -> u.getId()).orElse(null);
            if (me == null || !me.equals(sub.getUser().getId())) {
                return ResponseEntity.status(403).build();
            }
        }
        return ResponseEntity.ok(subscriptionMapper.toDto(sub));
    }

    @Override
    public ResponseEntity<SubscriptionDto> createSubscription(CreateSubscriptionRequestDto body, String idempotencyKey) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (!isAdmin) {
            var me = userRepo.findByEmail(auth.getName()).map(u -> u.getId()).orElse(null);
            if (me == null || !me.equals(body.getUserId())) {
                return ResponseEntity.status(403).build();
            }
        }

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            String fp = fingerprint("POST", "/api/subscriptions", body.getUserId(), body.getPrice(), body.getBillingDayOfMonth(), auth != null ? auth.getName() : "");
            var existing = idempotencyRepo.findByKey(idempotencyKey).orElse(null);
            if (existing != null) {
                if (!existing.getFingerprint().equals(fp)) {
                    return ResponseEntity.status(409).build();
                }
                try {
                    var dto = objectMapper.readValue(existing.getResponseBody(), SubscriptionDto.class);
                    return ResponseEntity.status(existing.getResponseStatus()).body(dto);
                } catch (Exception e) {
                    return ResponseEntity.status(409).build();
                }
            }
            var created = subscriptionService.create(
                    new CreateSubscriptionCommand(
                            body.getUserId(),
                            new BigDecimal(body.getPrice()),
                            body.getBillingDayOfMonth()
                    )
            );
            var dto = subscriptionMapper.toDto(created);
            try {
                var json = objectMapper.writeValueAsString(dto);
                idempotencyRepo.save(IdempotencyKey.builder()
                        .key(idempotencyKey)
                        .method("POST")
                        .path("/api/subscriptions")
                        .fingerprint(fp)
                        .responseStatus(200)
                        .responseBody(json)
                        .userId(body.getUserId())
                        .build());
            } catch (Exception ignored) {}
            return ResponseEntity.ok(dto);
        }

        var created = subscriptionService.create(
                new CreateSubscriptionCommand(
                        body.getUserId(),
                        new BigDecimal(body.getPrice()),
                        body.getBillingDayOfMonth()
                )
        );
        return ResponseEntity.ok(subscriptionMapper.toDto(created));
    }

    @Override
    public ResponseEntity<Void> processSubscriptionNow(Long subscriptionId, String idempotencyKey) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        var sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + subscriptionId));
        if (!isAdmin) {
            var me = userRepo.findByEmail(auth.getName()).map(u -> u.getId()).orElse(null);
            if (me == null || !me.equals(sub.getUser().getId())) {
                return ResponseEntity.status(403).build();
            }
        }

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            String fp = fingerprint("POST", "/api/subscriptions/" + subscriptionId + "/process", sub.getUser().getId(), sub.getPrice().toPlainString(), sub.getBillingDayOfMonth(), auth != null ? auth.getName() : "");
            var existing = idempotencyRepo.findByKey(idempotencyKey).orElse(null);
            if (existing != null) {
                if (!existing.getFingerprint().equals(fp)) {
                    return ResponseEntity.status(409).build();
                }
                return ResponseEntity.status(existing.getResponseStatus()).build();
            }
            paymentProcessingService.processForSubscription(sub);
            idempotencyRepo.save(IdempotencyKey.builder()
                    .key(idempotencyKey)
                    .method("POST")
                    .path("/api/subscriptions/" + subscriptionId + "/process")
                    .fingerprint(fp)
                    .responseStatus(202)
                    .responseBody("{}")
                    .userId(sub.getUser().getId())
                    .build());
            return ResponseEntity.accepted().build();
        }

        paymentProcessingService.processForSubscription(sub);
        return ResponseEntity.accepted().build();
    }

    private String fingerprint(String method, String path, Object... parts) {
        var base = method + "|" + path + "|" + String.join("|", java.util.Arrays.stream(parts).map(p -> p == null ? "null" : p.toString()).toList());
        return DigestUtils.appendMd5DigestAsHex(base.getBytes(StandardCharsets.UTF_8), new StringBuilder()).toString();
    }
}
