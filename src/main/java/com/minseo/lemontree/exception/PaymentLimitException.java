package com.minseo.lemontree.exception;

/**
 * class: PaymentLimitException.
 *
 * @author devminseo
 * @version 8/10/24
 */
public class PaymentLimitException extends RuntimeException {
    public static final String MSG = " 결제 한도를 넘어 섰습니다.";
    public PaymentLimitException(String type) {
        super(type + MSG);
    }
}
