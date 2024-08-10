package com.minseo.lemontree.entity;

import com.minseo.lemontree.converter.HistoryTypeConverter;
import com.minseo.lemontree.domain.HistoryType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * class: History.
 * 결제/결제취소/페이백/페이백취소 기록을 가지는 엔티티 클래스입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */

@Table(name = "history")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Column(name = "order_id")
    private Long orderId;

    @NotNull
    @Column(name = "type")
    @Convert(converter = HistoryTypeConverter.class)
    private HistoryType historyType;

    @NotNull
    @Column(name = "money")
    private Long money;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public History(Member member, Long orderId, HistoryType historyType, Long money) {
        this.member = member;
        this.orderId = orderId;
        this.historyType = historyType;
        this.money = money;
        this.createdAt = LocalDateTime.now();
    }
}
