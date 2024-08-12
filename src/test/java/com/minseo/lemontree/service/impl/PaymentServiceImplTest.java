package com.minseo.lemontree.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.minseo.lemontree.domain.HistoryType;
import com.minseo.lemontree.dto.request.PaymentCancelRequest;
import com.minseo.lemontree.dto.request.PaymentRequest;
import com.minseo.lemontree.dummy.MemberDummy;
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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * class: PaymentServiceImplTest.
 * 결제 서비스 테스트 입니다.
 *
 * @author devminseo
 * @version 8/12/24
 */
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    HistoryRepository historyRepository;
    @Mock
    HistoryService historyService;
    @Mock
    AmountUsedService amountUsedService;

    private Member member;
    private Member inActiveMember;
    private PaymentRequest paymentRequestDto;
    private PaymentCancelRequest cancelRequestDto;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();
        inActiveMember = MemberDummy.dummyInActive();

        paymentRequestDto = new PaymentRequest();
        ReflectionTestUtils.setField(paymentRequestDto, "memberId", member.getMemberId());
        ReflectionTestUtils.setField(paymentRequestDto, "productPrice", 1000L);
        ReflectionTestUtils.setField(paymentRequestDto, "orderId", 10L);

        cancelRequestDto = new PaymentCancelRequest();
        ReflectionTestUtils.setField(cancelRequestDto, "memberId", member.getMemberId());
        ReflectionTestUtils.setField(cancelRequestDto, "orderId", 10L);
    }

    @Test
    @DisplayName("결제 - 성공 테스트")
    void paymentSuccess() {
        when(memberRepository.findWithPessimisticLockByMemberId(paymentRequestDto.getMemberId())).thenReturn(
                Optional.of(member));
        when(historyService.checkHistoryExists(member, paymentRequestDto.getOrderId(), HistoryType.PAYMENT)).thenReturn(
                false);

        paymentService.payment(paymentRequestDto);

        verify(amountUsedService).resetAmountUsed(member);
        verify(amountUsedService).checkAmountUsed(member, paymentRequestDto.getProductPrice());
        verify(memberRepository).findWithPessimisticLockByMemberId(paymentRequestDto.getMemberId());
        verify(historyService).saveHistory(eq(member), eq(paymentRequestDto.getProductPrice()), eq(
                paymentRequestDto.getOrderId()), eq(HistoryType.PAYMENT));
        assertEquals(9000L, member.getBalance());
    }

    @Test
    @DisplayName("결제 - 회원을 찾을 수 없을 때 테스트")
    void paymentMemberNotFoundException() {
        when(memberRepository.findWithPessimisticLockByMemberId(paymentRequestDto.getMemberId())).thenReturn(
                Optional.empty());
        assertThrows(MemberNotFoundException.class, () -> paymentService.payment(paymentRequestDto));
    }

    @Test
    @DisplayName("결제 - 결제 중복 테스트")
    void paymentAlreadyOrderedException() {
        when(memberRepository.findWithPessimisticLockByMemberId(paymentRequestDto.getMemberId())).thenReturn(
                Optional.of(member));
        when(historyService.checkHistoryExists(member, paymentRequestDto.getOrderId(), HistoryType.PAYMENT)).thenReturn(
                true);

        assertThrows(AlreadyOrderedException.class, () -> paymentService.payment(paymentRequestDto));
    }

    @Test
    @DisplayName("결제 - 탈퇴한 회원 테스트")
    void paymentMemberInActiveException() {
        when(memberRepository.findWithPessimisticLockByMemberId(paymentRequestDto.getMemberId())).thenReturn(
                Optional.of(inActiveMember));

        assertThrows(MemberInActiveException.class, () -> paymentService.payment(paymentRequestDto));
    }

    @Test
    @DisplayName("결제 - 잔액 부족 테스트")
    void paymentInsufficientBalanceException() {
        ReflectionTestUtils.setField(paymentRequestDto, "productPrice", 11000L);
        when(memberRepository.findWithPessimisticLockByMemberId(paymentRequestDto.getMemberId())).thenReturn(
                Optional.of(member));

        assertThrows(InsufficientBalanceException.class, () -> paymentService.payment(paymentRequestDto));
    }

    @Test
    @DisplayName("결제 - 1회 한도 초과")
    void paymentLimitException() {
        ReflectionTestUtils.setField(paymentRequestDto, "productPrice", 3500L);
        when(memberRepository.findWithPessimisticLockByMemberId(paymentRequestDto.getMemberId())).thenReturn(
                Optional.of(member));

        assertThrows(PaymentLimitException.class, () -> paymentService.payment(paymentRequestDto));
    }

    @Test
    @DisplayName("결제 취소 - 성공 테스트")
    void paymentCancelSuccess() {
        History orderSheet = History.builder()
                .member(member)
                .orderId(cancelRequestDto.getOrderId())
                .historyType(HistoryType.PAYMENT)
                .money(1000L)
                .build();

        member.updateBalance(9000L);

        when(memberRepository.findWithPessimisticLockByMemberId(cancelRequestDto.getMemberId())).thenReturn(
                Optional.of(member));
        when(historyService.checkHistoryExists(member, cancelRequestDto.getOrderId(),
                HistoryType.PAYMENT_CANCEL)).thenReturn(false);
        when(historyRepository.findByMemberAndOrderIdAndHistoryType(member, cancelRequestDto.getOrderId(),
                HistoryType.PAYMENT)).thenReturn(Optional.of(orderSheet));

        paymentService.paymentCancel(cancelRequestDto);

        verify(amountUsedService).resetAmountUsed(member);
        verify(amountUsedService).updateCancelAmountUsed(member, orderSheet);
        verify(historyService).saveHistory(eq(member), eq(orderSheet.getMoney()), eq(cancelRequestDto.getOrderId()),
                eq(HistoryType.PAYMENT_CANCEL));
        assertEquals(10000L, member.getBalance());

    }

    @Test
    @DisplayName("결제 취소 - 탈퇴한 회원 테스트")
    void paymentCancelMemberInActiveException() {
        when(memberRepository.findWithPessimisticLockByMemberId(cancelRequestDto.getMemberId())).thenReturn(
                Optional.of(inActiveMember));

        assertThrows(MemberInActiveException.class, () -> paymentService.paymentCancel(cancelRequestDto));
    }

    @Test
    @DisplayName("결제 취소 - 결제 내역을 찾을 수 없을 때 테스트")
    void paymentCancelHistoryNotFoundException() {
        when(memberRepository.findWithPessimisticLockByMemberId(cancelRequestDto.getMemberId())).thenReturn(
                Optional.of(member));
        when(historyService.checkHistoryExists(member, cancelRequestDto.getOrderId(),
                HistoryType.PAYMENT_CANCEL)).thenReturn(false);
        when(historyRepository.findByMemberAndOrderIdAndHistoryType(member, cancelRequestDto.getOrderId(),
                HistoryType.PAYMENT)).thenReturn(Optional.empty());

        assertThrows(HistoryNotFoundException.class, () -> paymentService.paymentCancel(cancelRequestDto));
    }


}