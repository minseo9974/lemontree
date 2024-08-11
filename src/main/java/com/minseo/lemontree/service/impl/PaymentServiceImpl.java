package com.minseo.lemontree.service.impl;

import com.minseo.lemontree.domain.HistoryType;
import com.minseo.lemontree.dto.request.PaymentCancelRequest;
import com.minseo.lemontree.dto.request.PaymentRequest;
import com.minseo.lemontree.entity.History;
import com.minseo.lemontree.entity.Member;
import com.minseo.lemontree.exception.AlreadyOrderedException;
import com.minseo.lemontree.exception.HistoryNotFoundException;
import com.minseo.lemontree.exception.InsufficientBalanceException;
import com.minseo.lemontree.exception.MemberInActiveException;
import com.minseo.lemontree.exception.MemberNotFoundException;
import com.minseo.lemontree.exception.PaymentLimitException;
import com.minseo.lemontree.repository.HistoryRepository;
import com.minseo.lemontree.repository.MemberRepository;
import com.minseo.lemontree.service.AmountUsedService;
import com.minseo.lemontree.service.HistoryService;
import com.minseo.lemontree.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * class: PaymentServiceImpl.
 * 결제/취소 로직 구현 클래스입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final HistoryService historyService;
    private final AmountUsedService amountUsedService;


    /**
     * 1. 유효성 검사
     * 2. 결제 중복 체크 검사
     * 3. 사용자 상태 검사
     * 4. 잔액 검사
     * 5. 결제 한도 검사
     * 6. 결제 진행
     * 7. history 기록
     *
     * @param paymentRequest 결제 정보
     */
    @Transactional(propagation = Propagation.REQUIRED, timeout = 5)
    @Override
    public void payment(PaymentRequest paymentRequest) {
        Member member = memberRepository.findWithPessimisticLockByMemberId(paymentRequest.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        // 이미 구매한 주문서 중복 구매 불가 로직
        if (historyService.checkHistoryExists(member, paymentRequest.getOrderId(), HistoryType.PAYMENT)) {
            throw new AlreadyOrderedException("결제");
        }

        if (!member.isActive()) {
            throw new MemberInActiveException();
        }
        if (member.getBalance().compareTo(paymentRequest.getProductPrice()) < 0) {
            throw new InsufficientBalanceException();
        }
        //1회 제한
        if (paymentRequest.getProductPrice().compareTo(member.getOnceLimit()) > 0) {
            throw new PaymentLimitException("1회");
        }
        //일,월 제한 업데이트
        amountUsedService.resetAmountUsed(member);
        amountUsedService.checkAmountUsed(member, paymentRequest.getProductPrice());

        //결제 처리
        member.updateBalance(Math.subtractExact(member.getBalance(), paymentRequest.getProductPrice()));

        // 사용량 업데이트
        amountUsedService.updateAmountUsed(member, paymentRequest.getProductPrice());

        //history 기록
        historyService.saveHistory(member, paymentRequest.getProductPrice(), paymentRequest.getOrderId(),
                HistoryType.PAYMENT);
    }


    /**
     * 1. 유효성 검사
     * 2. 결제 취소 중복 검사
     * 3. 결제 취소 가능 여부 검사
     * 4. 결제 취소 처리
     * 5. 사용량 업데이트 (결제했던 일/월 기준)
     * 6. history 기록
     *
     * @param cancelRequest 결제 취소 정보
     */
    @Transactional(propagation = Propagation.REQUIRED, timeout = 5)
    @Override
    public void paymentCancel(PaymentCancelRequest cancelRequest) {
        Member member = memberRepository.findWithPessimisticLockByMemberId(cancelRequest.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        if (!member.isActive()) {
            throw new MemberInActiveException();
        }
        if (historyService.checkHistoryExists(member, cancelRequest.getOrderId(), HistoryType.PAYMENT_CANCEL)) {
            throw new AlreadyOrderedException("결제 취소");
        }

        History orderSheet = historyRepository.findByMemberAndOrderIdAndHistoryType(member, cancelRequest.getOrderId(),
                        HistoryType.PAYMENT)
                .orElseThrow(() -> new HistoryNotFoundException(HistoryType.PAYMENT.getParameter()));

        Long refundAmount = orderSheet.getMoney();
        Long maxBalance = member.getMaxBalance();
        Long memberCurrentBalance = member.getBalance();

        // 사용량 업데이트
        amountUsedService.resetAmountUsed(member);
        amountUsedService.updateCancelAmountUsed(member, orderSheet);

        if (Math.addExact(memberCurrentBalance, refundAmount) > maxBalance) {
            member.updateBalance(maxBalance);
            refundAmount = Math.subtractExact(maxBalance, memberCurrentBalance);
        } else {
            member.updateBalance(Math.addExact(memberCurrentBalance, refundAmount));
        }

        historyService.saveHistory(member, refundAmount, cancelRequest.getOrderId(), HistoryType.PAYMENT_CANCEL);


    }
}
