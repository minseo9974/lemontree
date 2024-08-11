package com.minseo.lemontree.controller;

import com.minseo.lemontree.annotation.Auth;
import com.minseo.lemontree.dto.request.PaybackCancelRequest;
import com.minseo.lemontree.dto.request.PaybackRequest;
import com.minseo.lemontree.service.PaybackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * class: PaybackController.
 * 결제 후 페이백 지급 API, 페이백 취소 API
 *
 * @author devminseo
 * @version 8/11/24
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/payback")
@Slf4j
public class PaybackController {
    private final PaybackService paybackService;

    @Auth
    @PostMapping()
    public ResponseEntity<String> paybackRequest(@Valid @RequestBody PaybackRequest request) {
        paybackService.payback(request);

        return ResponseEntity.ok(request.getMemberId() + "님의 Payback Success!");
    }

    @Auth
    @PostMapping("/cancel")
    public ResponseEntity<String> paybackCancelRequest(@Valid @RequestBody PaybackCancelRequest request) {
        paybackService.paybackCancel(request);

        return ResponseEntity.ok(request.getMemberId() + "님의 Payback Cancel Success!");
    }
}
