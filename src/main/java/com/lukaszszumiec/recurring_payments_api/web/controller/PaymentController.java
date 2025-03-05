package com.lukaszszumiec.recurring_payments_api.web.controller;

import com.lukaszszumiec.recurring_payments_api.application.service.PaymentService;
import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("payments")
public class PaymentController {

    private final PaymentService paymentService;


    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public ResponseEntity<List<Payment>> getUserPayments(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(paymentService.getUserPayments(user));
    }
}
