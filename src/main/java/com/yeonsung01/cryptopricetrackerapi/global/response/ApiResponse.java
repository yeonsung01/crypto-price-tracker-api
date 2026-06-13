package com.yeonsung01.cryptopricetrackerapi.global.response;

import lombok.Getter;

/**
 * 공통 API 응답 래퍼
 * - 모든 응답을 { success, message, data } 형태로 통일
 * - 디자인 패턴: Factory Method (of, success, fail 정적 팩토리 메서드)
 */
@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // 성공 응답 (데이터 없음)
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
