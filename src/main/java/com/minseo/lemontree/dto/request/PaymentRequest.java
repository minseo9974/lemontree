package com.minseo.lemontree.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * class: PaymentRequest.
 * 결제 요청 DTO 클래스입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */

@Getter
@NoArgsConstructor
public class PaymentRequest {
    @NotNull(message = "아이디를 입력해주세요.")
    @PositiveOrZero
    private Long memberId;

    @NotNull(message = "상품 금액을 입력해주세요.")
    @PositiveOrZero
    private Long productPrice;

    @NotNull(message = "주문서를 입력해주세요.")
    @PositiveOrZero
    private Long orderId;


}
