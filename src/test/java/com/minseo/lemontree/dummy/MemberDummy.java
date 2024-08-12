package com.minseo.lemontree.dummy;

import com.minseo.lemontree.domain.MemberStatus;
import com.minseo.lemontree.entity.Member;

/**
 * class: MemberDummy.
 * 테스트시 더미 회원을 만들기위한 공통 클래스 입니다.
 *
 * @author devminseo
 * @version 8/12/24
 */
public class MemberDummy {
    public static Member dummy() {
        return Member.builder()
                .memberStatus(MemberStatus.ACTIVE)
                .balance(10000L)
                .maxBalance(15000L)
                .onceLimit(3000L)
                .dayLimit(5000L)
                .monthLimit(9000L)
                .build();
    }

    public static Member dummy2() {
        return Member.builder()
                .memberStatus(MemberStatus.ACTIVE)
                .balance(50000L)
                .maxBalance(10000L)
                .onceLimit(1000L)
                .dayLimit(3000L)
                .monthLimit(5000L)
                .build();
    }
    public static Member dummy3() {
        return Member.builder()
                .memberStatus(MemberStatus.ACTIVE)
                .balance(50000L)
                .maxBalance(10000L)
                .onceLimit(10000L)
                .dayLimit(50000L)
                .monthLimit(90000L)
                .build();
    }

    public static Member dummyInActive() {
        return Member.builder()
                .memberStatus(MemberStatus.INACTIVE)
                .balance(10000L)
                .maxBalance(15000L)
                .onceLimit(3000L)
                .dayLimit(5000L)
                .monthLimit(9000L)
                .build();
    }

}
