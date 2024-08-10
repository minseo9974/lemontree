package com.minseo.lemontree.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * class: MemberStatus.
 * 회원 상태를 enum 타입으로 관리합니다.
 *
 * @author devminseo
 * @version 8/10/24
 */
@Getter
public enum MemberStatus {
    ACTIVE(0, "active"),
    INACTIVE(1, "inactive");

    private int dbValue;
    private String parameter;

    public static final Map<Integer, MemberStatus> dbValueMap = Arrays.stream(MemberStatus.values()).collect(
            Collectors.toMap(MemberStatus::getDbValue, Function.identity()));
    public static final Map<String, MemberStatus> parameterMap = Arrays.stream(MemberStatus.values()).collect(
            Collectors.toMap(MemberStatus::getParameter, Function.identity()));

    MemberStatus(int dbValue, String parameter) {
        this.dbValue = dbValue;
        this.parameter = parameter;
    }

    public static MemberStatus fromDbValue(Integer dbValue){
        return dbValueMap.get(dbValue);
    }

    public static MemberStatus fromParameter(String parameter) {
        return parameterMap.get(parameter);
    }
}
