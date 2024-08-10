package com.minseo.lemontree.service;

import com.minseo.lemontree.domain.HistoryType;
import com.minseo.lemontree.entity.Member;

/**
 * class: HistoryService.
 *
 * @author devminseo
 * @version 8/10/24
 */
public interface HistoryService {

    /**
     * 주문건이 중복으로 발생되지 않게 체크하는 메서드입니다.
     *
     * @param orderId 주문서 번호
     * @return boolean
     */
    boolean checkOrderIdExists(Member member, Long orderId);

    /**
     * 새로운 타입의 기록을 만드는 메서드입니다.
     *
     * @param member 회원
     * @param money 가격
     * @param orderId 주문서번호
     * @param type 결제/취소/페이백/페이백취소 종류
     */
    void saveHistory(Member member, Long money, Long orderId, HistoryType type);
}
