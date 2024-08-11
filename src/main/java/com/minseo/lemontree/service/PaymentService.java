package com.minseo.lemontree.service;

import com.minseo.lemontree.dto.request.PaymentCancelRequest;
import com.minseo.lemontree.dto.request.PaymentRequest;

/**
 * class: PaymentService.
 * 결제/취소 서비스 인터페이스입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */
public interface PaymentService {

    /**
     * 결제하는 로직입니다.
     *
     * @param paymentRequest 결제정보
     */
    void payment(PaymentRequest paymentRequest);

    /**
     * 결제 취소하는 로직입니다.
     *
     * @param cancelRequest 결제 취소 정보
     */
    void paymentCancel(PaymentCancelRequest cancelRequest);
}
