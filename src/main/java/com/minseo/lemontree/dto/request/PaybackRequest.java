package com.minseo.lemontree.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * class: PaybackRequest.
 * 회원의 주문건에 대해 페이백을 요청하는 DTO 클래스입니다.
 *
 * @author devminseo
 * @version 8/11/24
 */
@Getter
@NoArgsConstructor
public class PaybackRequest {
    @NotNull(message = "아이디를 입력해주세요.")
    @PositiveOrZero
    private Long memberId;

    @NotNull(message = "주문서를 입력해주세요.")
    @PositiveOrZero
    private Long orderId;
}
