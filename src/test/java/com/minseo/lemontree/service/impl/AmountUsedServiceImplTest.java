package com.minseo.lemontree.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.minseo.lemontree.domain.HistoryType;
import com.minseo.lemontree.dummy.MemberDummy;
import com.minseo.lemontree.entity.AmountUsed;
import com.minseo.lemontree.entity.History;
import com.minseo.lemontree.entity.Member;
import com.minseo.lemontree.exception.PaymentLimitException;
import com.minseo.lemontree.repository.AmountUsedRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * class: AmountUsedServiceImplTest.
 * 사용량 서비스 테스트 입니다.
 * @author devminseo
 * @version 8/13/24
 */

@ExtendWith(MockitoExtension.class)
class AmountUsedServiceImplTest {
    @InjectMocks
    private AmountUsedServiceImpl amountUsedService;
    @Mock
    private AmountUsedRepository amountUsedRepository;

    private Member member;
    private AmountUsed amountUsed;
    private History history;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();

        amountUsed = AmountUsed.builder()
                .member(member)
                .dayAmountUsed(1000L)
                .monthAmountUsed(5000L)
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("사용량 초기화 테스트")
    void resetAmountUsedTest() {
        amountUsed.updateLastTime(LocalDateTime.now().minusMonths(1));
        when(amountUsedRepository.findAmountUsedByMember(member)).thenReturn(amountUsed);

        amountUsedService.resetAmountUsed(member);
        assertEquals(0L, amountUsed.getDayAmountUsed());
        assertEquals(0L, amountUsed.getMonthAmountUsed());
        verify(amountUsedRepository, times(1)).findAmountUsedByMember(member);
    }

    @Test
    @DisplayName("일/월 사용량 한도 초과 테스트")
    void checkAmountLimitTest() {
        when(amountUsedRepository.findAmountUsedByMember(member)).thenReturn(amountUsed);

        assertThrows(PaymentLimitException.class, () -> amountUsedService.checkAmountUsed(member, 9000L));

        verify(amountUsedRepository, times(1)).findAmountUsedByMember(member);
    }

    @Test
    @DisplayName("일/월 사용량 한도 성공 테스트")
    void checkAmountSuccessTest() {
        when(amountUsedRepository.findAmountUsedByMember(member)).thenReturn(amountUsed);

        assertDoesNotThrow(() -> amountUsedService.checkAmountUsed(member, 1500L));

        verify(amountUsedRepository, times(1)).findAmountUsedByMember(member);
    }

    @Test
    @DisplayName("일/월 사용량 업데이트 성공 테스트")
    void updatedAmountUsedSuccessTest() {
        when(amountUsedRepository.findAmountUsedByMember(member)).thenReturn(amountUsed);

        amountUsedService.updateAmountUsed(member, 3000L);

        assertEquals(4000L, amountUsed.getDayAmountUsed());
        assertEquals(8000L, amountUsed.getMonthAmountUsed());

        verify(amountUsedRepository, times(1)).findAmountUsedByMember(member);
    }

    @Test
    @DisplayName("결제 취소 시 사용량 테스트")
    void updateCancelAmountUsedTest() {
        when(amountUsedRepository.findAmountUsedByMember(member)).thenReturn(amountUsed);

        History orderSheet = History.builder()
                .member(member)
                .orderId(10L)
                .historyType(HistoryType.PAYMENT)
                .money(1000L)
                .build();

        amountUsedService.updateCancelAmountUsed(member, orderSheet);

        assertEquals(0L, amountUsed.getDayAmountUsed());
        assertEquals(4000L, amountUsed.getMonthAmountUsed());

        verify(amountUsedRepository, times(1)).findAmountUsedByMember(member);
    }

}