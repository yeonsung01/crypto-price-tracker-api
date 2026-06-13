package com.yeonsung01.cryptopricetrackerapi.global.exception;

import org.springframework.http.HttpStatus;

/**
 * 비즈니스 로직 예외
 * - HTTP 상태코드를 함께 담아 컨트롤러까지 전파
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    // 자주 쓰는 팩토리 메서드
    public static BusinessException notFound(String message) {
        return new BusinessException(message, HttpStatus.NOT_FOUND);
    }

    public static BusinessException conflict(String message) {
        return new BusinessException(message, HttpStatus.CONFLICT);
    }

    public static BusinessException badRequest(String message) {
        return new BusinessException(message, HttpStatus.BAD_REQUEST);
    }
}
