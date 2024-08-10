package com.minseo.lemontree.service.impl;

import com.minseo.lemontree.entity.AmountUsed;
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

        Long day = amountUsed.getDayAmountUsed() + amount;
        Long month = amountUsed.getMonthAmountUsed() + amount;

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

        Long dayAmount = amountUsed.getDayAmountUsed() + amount;
        Long monthAmount = amountUsed.getMonthAmountUsed() + amount;

        amountUsed.updateMonthAmountUsed(monthAmount);
        amountUsed.updateDayAmountUsed(dayAmount);

        LocalDateTime now = LocalDateTime.now();
        amountUsed.updateLastTime(now);
    }
}
