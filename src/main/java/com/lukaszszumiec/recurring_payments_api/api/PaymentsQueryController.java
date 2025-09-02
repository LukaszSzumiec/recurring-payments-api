package com.lukaszszumiec.recurring_payments_api.api;

import com.lukaszszumiec.recurring_payments_api.application.usecase.GetPaymentsUseCase;
import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentsQueryController {

    private final GetPaymentsUseCase getPayments;

    public PaymentsQueryController(GetPaymentsUseCase getPayments) {
        this.getPayments = getPayments;
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Payment>> listByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(getPayments.getByUserId(userId));
    }
}
