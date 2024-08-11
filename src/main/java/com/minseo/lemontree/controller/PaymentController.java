package com.minseo.lemontree.controller;

import com.minseo.lemontree.annotation.Auth;
import com.minseo.lemontree.dto.request.PaymentCancelRequest;
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
 * 머니 결제 API, 결제 취소 API
 *
 * @author devminseo
 * @version 8/10/24
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @Auth
    @PostMapping()
    public ResponseEntity<String> paymentRequest(@Valid @RequestBody PaymentRequest request) {
        paymentService.payment(request);

        return ResponseEntity.ok(request.getMemberId() + "님의 Payment Success!");
    }

    @Auth
    @PostMapping("/cancel")
    public ResponseEntity<String> paymentCancelRequest(@Valid @RequestBody PaymentCancelRequest request) {
        paymentService.paymentCancel(request);

        return ResponseEntity.ok("님의 Payment Cancel Success!");
    }
}
