package com.minseo.lemontree.exception;

/**
 * class: MemberInActiveException.
 *
 * @author devminseo
 * @version 8/10/24
 */
public class MemberInActiveException extends RuntimeException {
    private static final String MSG = "탈퇴한 회원입니다.";

    public MemberInActiveException() {
        super(MSG);
    }
}
