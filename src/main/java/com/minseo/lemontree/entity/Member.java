package com.minseo.lemontree.entity;

import com.minseo.lemontree.converter.MemberStatusConverter;
import com.minseo.lemontree.domain.MemberStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * class: Member.
 * 회원 정보를 가지는 엔티티입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */

@Table(name = "member")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @NotNull
    @Column(name = "status")
    @Convert(converter = MemberStatusConverter.class)
    private MemberStatus memberStatus;

    @NotNull
    @Column(name = "balance")
    private Long balance;

    @NotNull
    @Column(name = "max_balance")
    private Long maxBalance;

    @NotNull
    @Column(name = "once_limit")
    private Long onceLimit;

    @NotNull
    @Column(name = "day_limit")
    private Long dayLimit;

    @NotNull
    @Column(name = "month_limit")
    private Long monthLimit;

    public boolean isActive(){
        if (this.getMemberStatus().equals(MemberStatus.ACTIVE)) {
            return true;
        }
        return false;
    }

    public void updateBalance(Long balance) {
        this.balance = balance;
    }

    @Builder
    public Member(MemberStatus memberStatus, Long balance, Long maxBalance, Long onceLimit, Long dayLimit,
                  Long monthLimit) {
        this.memberStatus = memberStatus;
        this.balance = balance;
        this.maxBalance = maxBalance;
        this.onceLimit = onceLimit;
        this.dayLimit = dayLimit;
        this.monthLimit = monthLimit;
    }
}
