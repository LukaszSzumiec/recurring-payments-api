package com.lukaszszumiec.recurring_payments_api.api;

import com.lukaszszumiec.recurring_payments_api.application.PaymentQueryService;
import com.lukaszszumiec.recurring_payments_api.api.mapper.PaymentMapper;
import com.lukaszszumiec.recurring_payments_api.generated.api.PaymentsApi;
import com.lukaszszumiec.recurring_payments_api.generated.model.PaymentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaymentsQueryController implements PaymentsApi {

    private final PaymentQueryService paymentQueryService;
    private final PaymentMapper paymentMapper;

    public PaymentsQueryController(PaymentQueryService paymentQueryService, PaymentMapper paymentMapper) {
        this.paymentQueryService = paymentQueryService;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public ResponseEntity<List<PaymentDto>> listPaymentsForUser(Long userId) {
        var list = paymentQueryService.getByUserId(userId)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<List<PaymentDto>> listPaymentsByUserDEPRECATED(Long userId) {
        return listPaymentsForUser(userId);
    }
}
