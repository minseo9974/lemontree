package com.minseo.lemontree.controller;

import com.minseo.lemontree.dto.ApiError;
import com.minseo.lemontree.exception.AlreadyOrderedException;
import com.minseo.lemontree.exception.InsufficientBalanceException;
import com.minseo.lemontree.exception.MemberInActiveException;
import com.minseo.lemontree.exception.MemberNotFoundException;
import com.minseo.lemontree.exception.PaymentLimitException;
import com.minseo.lemontree.exception.UnAuthorizationException;
import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * class: GlobalControllerAdvice.
 * 애플리케이션 전역에서 발생하는 예외를 처리하기위한 글로벌 예외 처리 클래스입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({UnAuthorizationException.class, MemberInActiveException.class})
    public ResponseEntity<ApiError> handleUnAuthorizationException(Exception e) {
        return new ResponseEntity<>(new ApiError(false, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException e) {
        List<String> result = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return new ResponseEntity<>(new ApiError(false, result.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MemberNotFoundException.class})
    public ResponseEntity<ApiError> handleMemberNotFoundException(Exception e) {
        return new ResponseEntity<>(new ApiError(false, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InsufficientBalanceException.class, PaymentLimitException.class})
    public ResponseEntity<ApiError> handleBadRequestException(Exception e) {
        return new ResponseEntity<>(new ApiError(false, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AlreadyOrderedException.class})
    public ResponseEntity<ApiError> handleConflictException(Exception e) {
        return new ResponseEntity<>(new ApiError(false, e.getMessage()), HttpStatus.CONFLICT);
    }
}
