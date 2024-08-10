package com.minseo.lemontree.exception;

/**
 * class: InsufficientBalanceException.
 *
 * @author devminseo
 * @version 8/10/24
 */
public class InsufficientBalanceException extends RuntimeException {
    private static final String MSG = "잔액이 부족합니다.";

    public InsufficientBalanceException() {
        super(MSG);
    }
}
