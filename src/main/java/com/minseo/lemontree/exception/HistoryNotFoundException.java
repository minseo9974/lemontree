package com.minseo.lemontree.exception;

/**
 * class: HistoryNotFoundException.
 *
 * @author devminseo
 * @version 8/11/24
 */
public class HistoryNotFoundException extends RuntimeException {
    private static final String MSG = " 건을 찾을 수 없습니다.";
    public HistoryNotFoundException(String type) {
        super(type + MSG);
    }
}
