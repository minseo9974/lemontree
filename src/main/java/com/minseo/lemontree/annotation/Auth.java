package com.minseo.lemontree.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * class: Auth.
 * 컨트롤러에서 간단한 사용자 인증을 위한 어노테이션입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
}
