package com.minseo.lemontree.controller;

import com.minseo.lemontree.annotation.Auth;
import com.minseo.lemontree.dto.request.PaymentRequest;
import com.minseo.lemontree.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * class: MoneyPaymentController.
 *
 * @author devminseo
 * @version 8/10/24
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Slf4j
public class MoneyPaymentController {
    private final PaymentService paymentService;

    @Auth
    @PostMapping()
    public ResponseEntity<String> paymentRequest(@Valid @RequestBody PaymentRequest request) {
        paymentService.payment(request);
        return ResponseEntity.ok("Payment Success");
    }
}
