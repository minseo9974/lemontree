package com.minseo.lemontree.service;

import com.minseo.lemontree.entity.History;
import com.minseo.lemontree.entity.Member;

/**
 * class: AmountUsedService.
 *
 * @author devminseo
 * @version 8/10/24
 */
public interface AmountUsedService {

    /**
     * 하루가 지나거나, 한달이 지나면 사용량을 초기화 하는 메서드입니다.
     *
     * @param member 회원식별
     */
    void resetAmountUsed(Member member);

    /**
     * 구매한도를 넘지 않았는지 검사하는 메서드입니다.
     *
     * @param member 회원식별
     * @param amount 가격
     */
    void checkAmountUsed(Member member, Long amount);

    /**
     * 결제 후 사용금액을 사용량에 업데이트하는 메서드입니다.
     *
     * @param member 회원식별
     * @param amount 추가할 금액
     */
    void updateAmountUsed(Member member, Long amount);

    /**
     * 결제 취소시 사용량을 일/월에 맞춰 업데이트합니다.
     *
     * @param member 회원 식별
     * @param history 업데이트할 정보
     */
    void updateCancelAmountUsed(Member member, History history);
}
