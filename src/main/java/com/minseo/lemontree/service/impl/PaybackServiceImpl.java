package com.minseo.lemontree.service.impl;

import com.minseo.lemontree.domain.HistoryType;
import com.minseo.lemontree.dto.request.PaybackRequest;
import com.minseo.lemontree.entity.History;
import com.minseo.lemontree.entity.Member;
import com.minseo.lemontree.exception.AlreadyOrderedException;
import com.minseo.lemontree.exception.HistoryNotFoundException;
import com.minseo.lemontree.exception.MemberInActiveException;
import com.minseo.lemontree.exception.MemberNotFoundException;
import com.minseo.lemontree.repository.HistoryRepository;
import com.minseo.lemontree.repository.MemberRepository;
import com.minseo.lemontree.service.HistoryService;
import com.minseo.lemontree.service.PaybackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * class: PaybackServiceImpl.
 *
 * @author devminseo
 * @version 8/11/24
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PaybackServiceImpl implements PaybackService {
    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final HistoryService historyService;

    /**
     * 1. 유효성 검사
     * 2. 사용자 상태 검사
     * 3. 결제 내역 검사
     * 4. 페이백 중복 체크 검사
     * 5. 머니 소지 한도 검사
     * 6. 회원 잔고 페이백 추가
     * 7. history 기록
     *
     * @param paybackRequest 페이백 하기위한 정보
     */
    @Transactional(propagation = Propagation.REQUIRED, timeout = 5)
    @Override
    public void payback(PaybackRequest paybackRequest) {
        Member member =
                memberRepository.findById(paybackRequest.getMemberId()).orElseThrow(MemberNotFoundException::new);

        if (!member.isActive()) {
            throw new MemberInActiveException();
        }

        History orderSheet = historyRepository.findWithPessimisticLockByMemberAndOrderIdAndHistoryType(member,
                        paybackRequest.getOrderId(), HistoryType.PAYMENT)
                .orElseThrow(() -> new HistoryNotFoundException(HistoryType.PAYMENT.getParameter()));

        if (historyService.checkHistoryExists(member, paybackRequest.getOrderId(), HistoryType.PAYBACK)) {
            throw new AlreadyOrderedException("페이백");
        }
        if (historyService.checkHistoryExists(member, paybackRequest.getOrderId(), HistoryType.PAYMENT_CANCEL)) {
            throw new AlreadyOrderedException("결제 취소");
        }

        long paybackMoney = Math.round(orderSheet.getMoney() * 0.1);
        Long maxBalance = member.getMaxBalance();
        Long memberCurrentBalance = member.getBalance();

        if (Math.addExact(memberCurrentBalance, paybackMoney) > maxBalance) {
            member.updateBalance(maxBalance);
            paybackMoney = Math.subtractExact(maxBalance, memberCurrentBalance);
        } else {
            member.updateBalance(Math.addExact(memberCurrentBalance, paybackMoney));
        }

        historyService.saveHistory(member, paybackMoney, paybackRequest.getOrderId(), HistoryType.PAYBACK);

    }
}
