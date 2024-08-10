package com.minseo.lemontree.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * class: HistoryStatus.
 * 결제/취소/페이백/페이백취소의 성공, 실패를 관리하는 enum.
 *
 * @author devminseo
 * @version 8/10/24
 */
@Getter
public enum HistoryStatus {
    SUCCESS(0, "success"),
    FAIL(1, "fail");


    private int dbValue;
    private String parameter;

    public static final Map<Integer, HistoryStatus> dbValueMap = Arrays.stream(HistoryStatus.values()).collect(
            Collectors.toMap(HistoryStatus::getDbValue, Function.identity()));
    public static final Map<String, HistoryStatus> parameterMap = Arrays.stream(HistoryStatus.values()).collect(
            Collectors.toMap(HistoryStatus::getParameter, Function.identity()));

    HistoryStatus(int dbValue, String parameter) {
        this.dbValue = dbValue;
        this.parameter = parameter;
    }

    public static HistoryStatus fromDbValue(Integer dbValue){
        return dbValueMap.get(dbValue);
    }

    public static HistoryStatus fromParameter(String parameter) {
        return parameterMap.get(parameter);
    }
}
