package com.minseo.lemontree.service.impl;

import com.minseo.lemontree.entity.AmountUsed;
import com.minseo.lemontree.entity.History;
import com.minseo.lemontree.entity.Member;
import com.minseo.lemontree.exception.PaymentLimitException;
import com.minseo.lemontree.repository.AmountUsedRepository;
import com.minseo.lemontree.service.AmountUsedService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * class: AmountUsedServiceImpl.
 *
 * @author devminseo
 * @version 8/10/24
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AmountUsedServiceImpl implements AmountUsedService {
    private final AmountUsedRepository amountUsedRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetAmountUsed(Member member) {
        AmountUsed amountUsed = amountUsedRepository.findAmountUsedByMember(member);

        LocalDate today = LocalDate.now();
        LocalDate lastUpdatedDate = amountUsed.getLastUpdate().toLocalDate();

        if (!lastUpdatedDate.equals(today)) {
            amountUsed.updateDayAmountUsed(0L);
        }
        YearMonth lastUpdatedMonth = YearMonth.from(amountUsed.getLastUpdate());
        YearMonth currentMonth = YearMonth.from(today);
        if (!lastUpdatedMonth.equals(currentMonth)) {
            amountUsed.updateMonthAmountUsed(0L);
        }

        LocalDateTime now = LocalDateTime.now();
        amountUsed.updateLastTime(now);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void checkAmountUsed(Member member, Long amount) {
        AmountUsed amountUsed = amountUsedRepository.findAmountUsedByMember(member);

        Long day = Math.addExact(amountUsed.getDayAmountUsed(), amount);
        Long month = Math.addExact(amountUsed.getMonthAmountUsed(), amount);

        if (day.compareTo(member.getDayLimit()) > 0 || month.compareTo(member.getMonthLimit()) > 0) {
            throw new PaymentLimitException("일 또는 월");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateAmountUsed(Member member, Long amount) {
        AmountUsed amountUsed = amountUsedRepository.findAmountUsedByMember(member);

        Long dayAmount = Math.addExact(amountUsed.getDayAmountUsed(), amount);
        Long monthAmount = Math.addExact(amountUsed.getMonthAmountUsed(), amount);

        amountUsed.updateMonthAmountUsed(monthAmount);
        amountUsed.updateDayAmountUsed(dayAmount);

        LocalDateTime now = LocalDateTime.now();
        amountUsed.updateLastTime(now);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCancelAmountUsed(Member member, History history) {
        AmountUsed amountUsed = amountUsedRepository.findAmountUsedByMember(member);

        LocalDate historyDate = history.getCreatedAt().toLocalDate();
        LocalDate amountDate = amountUsed.getLastUpdate().toLocalDate();

        if (amountDate.equals(historyDate)) {
            amountUsed.updateDayAmountUsed(Math.subtractExact(amountUsed.getDayAmountUsed(), history.getMoney()));
        }

        YearMonth historyMonth = YearMonth.from(historyDate);
        YearMonth amountMonth = YearMonth.from(amountDate);

        if (amountMonth.equals(historyMonth)) {
            amountUsed.updateMonthAmountUsed(Math.subtractExact(amountUsed.getMonthAmountUsed(), history.getMoney()));
        }

        LocalDateTime now = LocalDateTime.now();
        amountUsed.updateLastTime(now);
    }
}
