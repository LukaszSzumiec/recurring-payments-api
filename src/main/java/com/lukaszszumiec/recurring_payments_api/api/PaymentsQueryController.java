package com.lukaszszumiec.recurring_payments_api.api;

import com.lukaszszumiec.recurring_payments_api.application.PaymentQueryService;
import com.lukaszszumiec.recurring_payments_api.api.mapper.PaymentMapper;
import com.lukaszszumiec.recurring_payments_api.generated.api.PaymentsApi;
import com.lukaszszumiec.recurring_payments_api.generated.model.PaymentDto;
import com.lukaszszumiec.recurring_payments_api.generated.model.PaymentPageDto;
import com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.UserRepositoryAdapter;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class PaymentsQueryController implements PaymentsApi {

    private final PaymentQueryService paymentQueryService;
    private final PaymentMapper paymentMapper;
    private final UserRepositoryAdapter userRepo;

    public PaymentsQueryController(PaymentQueryService paymentQueryService,
                                   PaymentMapper paymentMapper,
                                   UserRepositoryAdapter userRepo) {
        this.paymentQueryService = paymentQueryService;
        this.paymentMapper = paymentMapper;
        this.userRepo = userRepo;
    }

    @Override
    public ResponseEntity<PaymentPageDto> listPaymentsForUser(Long userId, Integer page, Integer size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (!isAdmin) {
            var me = userRepo.findByEmail(auth.getName()).map(u -> u.getId()).orElse(null);
            if (me == null || !me.equals(userId)) {
                return ResponseEntity.status(403).build();
            }
        }
        int p = page == null || page < 0 ? 0 : page;
        int s = size == null || size < 1 ? 20 : Math.min(size, 200);
        var pageObj = paymentQueryService.getByUserId(userId, PageRequest.of(p, s));
        var dto = new PaymentPageDto();
        dto.setItems(pageObj.getContent().stream().map(paymentMapper::toDto).collect(Collectors.toList()));
        dto.setPage(pageObj.getNumber());
        dto.setSize(pageObj.getSize());
        dto.setTotalElements(pageObj.getTotalElements());
        dto.setTotalPages(pageObj.getTotalPages());
        return ResponseEntity.ok(dto);
    }
}
