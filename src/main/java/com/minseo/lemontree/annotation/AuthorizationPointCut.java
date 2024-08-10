package com.minseo.lemontree.annotation;

import com.minseo.lemontree.exception.UnAuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * class: AuthorizationPointCut.
 * 간단하게 사용자 인증을 위해 인증 헤더가 있는지 확인하는 AOP 클래스 입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationPointCut {
    public static final String HEADER = "Authorization";
    public static final String PWD = "lemontree";

    @Around(value = "@annotation(com.minseo.lemontree.annotation.Auth)")
    public Object isAuthorization(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = getRequest();

        String lemontree = request.getHeader(HEADER);

        if (!PWD.equals(lemontree)) {
            throw new UnAuthorizationException();
        }

        return pjp.proceed(pjp.getArgs());
    }

    /**
     * HttpServletRequest 객체를 통해 파라미터에 접근해 멤버 아이디를 가져오는 메서드입니다.
     *
     * @return memberId
     */
    private static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        return servletRequestAttributes.getRequest();
    }

}
