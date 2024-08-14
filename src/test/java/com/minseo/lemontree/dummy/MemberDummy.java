package com.minseo.lemontree.dummy;

import com.minseo.lemontree.domain.MemberStatus;
import com.minseo.lemontree.entity.AmountUsed;
import com.minseo.lemontree.entity.Member;
import java.time.LocalDateTime;

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

    public static AmountUsed amountUsedDummy(Member member) {
        return AmountUsed.builder()
                .member(member)
                .dayAmountUsed(0L)
                .monthAmountUsed(0L)
                .lastUpdate(LocalDateTime.now())
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
