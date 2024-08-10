package com.minseo.lemontree.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * class: AmountUsed.
 * 유저의 일별,월별 금액 사용량을 기록하는 엔티티입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */
@Table(name = "amount_used")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AmountUsed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amount_used_id")
    private Long amountUsedId;

    @NotNull
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Column(name = "day_amount_used")
    private Long dayAmountUsed;

    @NotNull
    @Column(name = "month_amount_used")
    private Long monthAmountUsed;

    @NotNull
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    public void updateDayAmountUsed(Long amount) {
        this.dayAmountUsed = amount;
    }

    public void updateMonthAmountUsed(Long amount) {
        this.monthAmountUsed = amount;
    }

    public void updateLastTime(LocalDateTime now) {
        this.lastUpdate = now;
    }
}
