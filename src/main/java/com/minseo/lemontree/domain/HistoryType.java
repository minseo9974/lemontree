package com.minseo.lemontree.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * class: HistoryStatus.
 * 결제/취소/페이백/페이백취소 타입을 관리하는 enum.
 *
 * @author devminseo
 * @version 8/10/24
 */
@Getter
public enum HistoryType {
    PAYMENT(0, "payment"),
    PAYMENT_CANCEL(1, "paymentCancel"),
    PAYBACK(2, "payback"),
    PAYBACK_CANCEL(3, "paybackCancel");

    private int dbValue;
    private String parameter;

    public static final Map<Integer, HistoryType> dbValueMap = Arrays.stream(HistoryType.values()).collect(
            Collectors.toMap(HistoryType::getDbValue, Function.identity()));
    public static final Map<String, HistoryType> parameterMap = Arrays.stream(HistoryType.values()).collect(
            Collectors.toMap(HistoryType::getParameter, Function.identity()));

    HistoryType(int dbValue, String parameter) {
        this.dbValue = dbValue;
        this.parameter = parameter;
    }

    public static HistoryType fromDbValue(Integer dbValue){
        return dbValueMap.get(dbValue);
    }

    public static HistoryType fromParameter(String parameter) {
        return parameterMap.get(parameter);
    }
}