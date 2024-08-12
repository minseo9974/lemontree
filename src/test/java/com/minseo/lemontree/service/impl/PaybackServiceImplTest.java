package com.minseo.lemontree.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.minseo.lemontree.domain.HistoryType;
import com.minseo.lemontree.dto.request.PaybackCancelRequest;
import com.minseo.lemontree.dto.request.PaybackRequest;
import com.minseo.lemontree.dummy.MemberDummy;
import com.minseo.lemontree.entity.History;
import com.minseo.lemontree.entity.Member;
import com.minseo.lemontree.exception.AlreadyOrderedException;
import com.minseo.lemontree.exception.HistoryNotFoundException;
import com.minseo.lemontree.exception.InsufficientBalanceException;
import com.minseo.lemontree.exception.MemberInActiveException;
import com.minseo.lemontree.exception.MemberNotFoundException;
import com.minseo.lemontree.repository.HistoryRepository;
import com.minseo.lemontree.repository.MemberRepository;
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
 * class: PaybackServiceImplTest.
 * 페이백 서비스 테스트 입니다.
 *
 * @author devminseo
 * @version 8/12/24
 */
@ExtendWith(MockitoExtension.class)
class PaybackServiceImplTest {
    @InjectMocks
    PaybackServiceImpl paybackService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    HistoryRepository historyRepository;
    @Mock
    HistoryService historyService;

    private Member member;
    private Member inActiveMember;
    private PaybackRequest paybackRequestDto;
    private PaybackCancelRequest cancelRequestDto;
    private History orderSheet;
    private History paybackSheet;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();
        inActiveMember = MemberDummy.dummyInActive();

        paybackRequestDto = new PaybackRequest();
        ReflectionTestUtils.setField(paybackRequestDto, "memberId", member.getMemberId());
        ReflectionTestUtils.setField(paybackRequestDto, "orderId", 10L);

        cancelRequestDto = new PaybackCancelRequest();
        ReflectionTestUtils.setField(cancelRequestDto, "memberId", member.getMemberId());
        ReflectionTestUtils.setField(cancelRequestDto, "orderId", 10L);

        orderSheet = History.builder()
                .member(member)
                .orderId(paybackRequestDto.getOrderId())
                .historyType(HistoryType.PAYMENT)
                .money(1000L)
                .build();

        paybackSheet = History.builder()
                .member(member)
                .orderId(paybackRequestDto.getOrderId())
                .historyType(HistoryType.PAYBACK)
                .money(100L)
                .build();
    }

    @Test
    @DisplayName("페이백 - 성공 테스트")
    void payback() {
        when(memberRepository.findById(paybackRequestDto.getMemberId())).thenReturn(Optional.of(member));
        when(historyRepository.findWithPessimisticLockByMemberAndOrderIdAndHistoryType(member,
                paybackRequestDto.getOrderId(), HistoryType.PAYMENT)).thenReturn(Optional.of(orderSheet));
        when(historyService.checkHistoryExists(member, paybackRequestDto.getOrderId(), HistoryType.PAYBACK)).thenReturn(
                false);
        when(historyService.checkHistoryExists(member, paybackRequestDto.getOrderId(),
                HistoryType.PAYMENT_CANCEL)).thenReturn(false);

        paybackService.payback(paybackRequestDto);

        verify(historyService).saveHistory(eq(member), eq(100L), eq(paybackRequestDto.getOrderId()),
                eq(HistoryType.PAYBACK));
        assertEquals(10100L, member.getBalance());
    }

    @Test
    @DisplayName("페이백 - 회원을 찾을 수 없을 때 테스트")
    void paybackMemberNotFoundException() {
        when(memberRepository.findById(paybackRequestDto.getMemberId())).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> paybackService.payback(paybackRequestDto));
    }

    @Test
    @DisplayName("페이백 - 탈퇴한 회원 테스트")
    void paybackMemberInActiveException() {
        when(memberRepository.findById(paybackRequestDto.getMemberId())).thenReturn(Optional.of(inActiveMember));

        assertThrows(MemberInActiveException.class, () -> paybackService.payback(paybackRequestDto));
    }

    @Test
    @DisplayName("페이백 - 페이백 중복 테스트")
    void paybackAlreadyOrderedException() {
        when(memberRepository.findById(paybackRequestDto.getMemberId())).thenReturn(Optional.of(member));
        when(historyRepository.findWithPessimisticLockByMemberAndOrderIdAndHistoryType(member,
                paybackRequestDto.getOrderId(),
                HistoryType.PAYMENT)).thenReturn(Optional.of(orderSheet));
        when(historyService.checkHistoryExists(member, paybackRequestDto.getOrderId(), HistoryType.PAYBACK)).thenReturn(
                true);

        assertThrows(AlreadyOrderedException.class, () -> paybackService.payback(paybackRequestDto));

    }

    @Test
    @DisplayName("페이백 - 결제 취소 내역이 있으면 페이백 하지 않습니다.")
    void paybackAlreadyPaymentCancelException() {
        when(memberRepository.findById(paybackRequestDto.getMemberId())).thenReturn(Optional.of(member));
        when(historyRepository.findWithPessimisticLockByMemberAndOrderIdAndHistoryType(member,
                paybackRequestDto.getOrderId(), HistoryType.PAYMENT)).thenReturn(Optional.of(orderSheet));
        when(historyService.checkHistoryExists(member, paybackRequestDto.getOrderId(), HistoryType.PAYBACK)).thenReturn(
                false);
        when(historyService.checkHistoryExists(member, paybackRequestDto.getOrderId(),
                HistoryType.PAYMENT_CANCEL)).thenReturn(true);

        assertThrows(AlreadyOrderedException.class, () -> paybackService.payback(paybackRequestDto));
    }

    @Test
    @DisplayName("페이백 취소 - 성공 테스트")
    void paybackCancelSuccess() {
        when(memberRepository.findById(cancelRequestDto.getMemberId())).thenReturn(Optional.of(member));
        when(historyRepository.findWithPessimisticLockByMemberAndOrderIdAndHistoryType(member,
                cancelRequestDto.getOrderId(), HistoryType.PAYBACK)).thenReturn(Optional.of(paybackSheet));
        when(historyService.checkHistoryExists(member, cancelRequestDto.getOrderId(),
                HistoryType.PAYBACK_CANCEL)).thenReturn(false);

        paybackService.paybackCancel(cancelRequestDto);

        verify(historyService).saveHistory(eq(member), eq(100L), eq(cancelRequestDto.getOrderId()),
                eq(HistoryType.PAYBACK_CANCEL));

        assertEquals(9900L, member.getBalance());
    }

    @Test
    @DisplayName("페이백 취소 - 회원을 찾을 수 없을 때 테스트")
    void paybackCancelMemberNotFoundException() {
        when(memberRepository.findById(cancelRequestDto.getMemberId())).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> paybackService.paybackCancel(cancelRequestDto));
    }

    @Test
    @DisplayName("페이백 취소 - 탈퇴한 회원 테스트")
    void paybackCancelMemberInActiveException() {
        when(memberRepository.findById(cancelRequestDto.getMemberId())).thenReturn(Optional.of(inActiveMember));

        assertThrows(MemberInActiveException.class, () -> paybackService.paybackCancel(cancelRequestDto));
    }

    @Test
    @DisplayName("페이백 취소 - 페이백 취소 중복 테스트")
    void paybackCancelAlreadyOrderedException() {
        when(memberRepository.findById(cancelRequestDto.getMemberId())).thenReturn(Optional.of(member));
        when(historyRepository.findWithPessimisticLockByMemberAndOrderIdAndHistoryType(member,
                cancelRequestDto.getOrderId(), HistoryType.PAYBACK)).thenReturn(Optional.of(paybackSheet));
        when(historyService.checkHistoryExists(member, cancelRequestDto.getOrderId(),
                HistoryType.PAYBACK_CANCEL)).thenReturn(true);

        assertThrows(AlreadyOrderedException.class, () -> paybackService.paybackCancel(cancelRequestDto));
    }

    @Test
    @DisplayName("페이백 취소 - 페이백 내역 찾을 수 없을 때 테스트")
    void paybackCancelHistoryNotFoundException() {
        when(memberRepository.findById(cancelRequestDto.getMemberId())).thenReturn(Optional.of(member));
        when(historyRepository.findWithPessimisticLockByMemberAndOrderIdAndHistoryType(member,
                cancelRequestDto.getOrderId(), HistoryType.PAYBACK)).thenReturn(Optional.empty());

        assertThrows(HistoryNotFoundException.class, () -> paybackService.paybackCancel(cancelRequestDto));
    }

    @Test
    @DisplayName("페이백 취소 - 잔액 부족 테스트")
    void paybackCancelInsufficientBalanceException() {
        member.updateBalance(0L);
        when(memberRepository.findById(cancelRequestDto.getMemberId())).thenReturn(Optional.of(member));
        when(historyRepository.findWithPessimisticLockByMemberAndOrderIdAndHistoryType(member,
                cancelRequestDto.getOrderId(), HistoryType.PAYBACK)).thenReturn(Optional.of(paybackSheet));
        when(historyService.checkHistoryExists(member, cancelRequestDto.getOrderId(),
                HistoryType.PAYBACK_CANCEL)).thenReturn(false);

        assertThrows(InsufficientBalanceException.class, () -> paybackService.paybackCancel(cancelRequestDto));
    }
}