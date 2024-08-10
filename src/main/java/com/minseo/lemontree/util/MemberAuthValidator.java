package com.minseo.lemontree.util;

import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * class: MemberAuthValidator.
 * 간단하게 사용자 인증을 위해 파라미터에 유저 id가 있는지 확인하는 클래스 입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */

@Component
public class MemberAuthValidator {
    private MemberAuthValidator() {
    }

    public static Boolean isValid(Long memberId) {
        if (Objects.isNull(memberId) || memberId < 0) {
            return false;
        }
        return true;
    }
}
