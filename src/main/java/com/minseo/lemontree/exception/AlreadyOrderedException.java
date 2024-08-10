package com.minseo.lemontree.exception;

/**
 * class: AlreadyOrderedException.
 *
 * @author devminseo
 * @version 8/10/24
 */
public class AlreadyOrderedException extends RuntimeException {
    public static final String MSG = "건이 완료 되었습니다.";

    public AlreadyOrderedException(String type) {
        super("이미 해당 " + type + MSG);
    }
}
