package com.minseo.lemontree.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.minseo.lemontree.domain.HistoryType;
import com.minseo.lemontree.dummy.MemberDummy;
import com.minseo.lemontree.entity.History;
import com.minseo.lemontree.entity.Member;
import com.minseo.lemontree.repository.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * class: HistoryServiceImplTest.
 * 내역 서비스 테스트 입니다.
 *
 * @author devminseo
 * @version 8/13/24
 */
@ExtendWith(MockitoExtension.class)
class HistoryServiceImplTest {
    @InjectMocks
    private HistoryServiceImpl historyService;
    @Mock
    private HistoryRepository historyRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();
    }

    @Test
    @DisplayName("내역이 존재할 때 테스트")
    void checkHistoryExistTest() {
        when(historyRepository.existsByMemberAndOrderIdAndHistoryType(member, 10L, HistoryType.PAYMENT)).thenReturn(
                true);

        boolean exist = historyService.checkHistoryExists(member, 10L, HistoryType.PAYMENT);

        assertTrue(exist);
        verify(historyRepository, times(1)).existsByMemberAndOrderIdAndHistoryType(member, 10L, HistoryType.PAYMENT);
    }

    @Test
    @DisplayName("내역이 존재하지 않을 때 테스트")
    void checkHistoryNonExistTest() {
        when(historyRepository.existsByMemberAndOrderIdAndHistoryType(member, 10L, HistoryType.PAYMENT)).thenReturn(
                false);

        boolean exist = historyService.checkHistoryExists(member, 10L, HistoryType.PAYMENT);

        assertFalse(exist);
        verify(historyRepository, times(1)).existsByMemberAndOrderIdAndHistoryType(member, 10L, HistoryType.PAYMENT);
    }

    @Test
    @DisplayName("내역 저장 테스트")
    void saveHistoryTest() {
        History history = History.builder()
                .member(member)
                .money(1000L)
                .historyType(HistoryType.PAYMENT)
                .orderId(10L)
                .build();

        historyService.saveHistory(member, 1000L, 10L, HistoryType.PAYMENT);

        verify(historyRepository, times(1)).save(any(History.class));
    }
}