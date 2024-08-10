package com.minseo.lemontree.service.impl;

import com.minseo.lemontree.domain.HistoryType;
import com.minseo.lemontree.entity.History;
import com.minseo.lemontree.entity.Member;
import com.minseo.lemontree.repository.HistoryRepository;
import com.minseo.lemontree.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * class: HistoryServiceImpl.
 *
 * @author devminseo
 * @version 8/10/24
 */

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public boolean checkOrderIdExists(Member member, Long orderId) {
        return historyRepository.existsByMemberAndOrderId(member, orderId);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveHistory(Member member, Long money, Long orderId, HistoryType type) {
        History history = History.builder()
                .member(member)
                .money(money)
                .historyType(type)
                .orderId(orderId)
                .build();

        historyRepository.save(history);
    }
}
