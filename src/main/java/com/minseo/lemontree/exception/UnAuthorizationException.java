package com.minseo.lemontree.exception;

/**
 * class: UnAuthorizationException.
 * 회원 인증이 틀렸을때 던지는 예외 입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */
public class UnAuthorizationException extends RuntimeException {
    private static final String MSG = "권한이 존재하지 않습니다.";

    public UnAuthorizationException() {
        super(MSG);
    }
}
