package com.minseo.lemontree.exception;

/**
 * class: MemberNotFoundException.
 *
 * @author devminseo
 * @version 8/10/24
 */

public class MemberNotFoundException extends RuntimeException{
    private static final String MSG = "존재하지 않는 회원입니다.";

    public MemberNotFoundException() {
        super(MSG);
    }
}
