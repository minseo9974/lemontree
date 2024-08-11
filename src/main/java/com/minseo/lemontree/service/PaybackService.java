package com.minseo.lemontree.service;

import com.minseo.lemontree.dto.request.PaybackRequest;

/**
 * class: PaybackService.
 * 페이백/페이백 취소 서비스 인터페이스 입니다.
 *
 * @author devminseo
 * @version 8/11/24
 */
public interface PaybackService {

    /**
     * 페이백하는 로직입니다.
     *
     * @param paybackRequest 페이백 하기위한 정보
     */
    void payback(PaybackRequest paybackRequest);
}
