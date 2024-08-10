package com.minseo.lemontree.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * class: ApiError.
 * api 응답에서 발생할 수 있는 오류를 간단하게 캡슐화 하는 클래스입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private boolean success;
    private String message;
}
