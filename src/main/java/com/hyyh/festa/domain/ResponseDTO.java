package com.hyyh.festa.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDTO<T> {

    private final int status;
    private final String message;
    private final T data;

    private ResponseDTO(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 기본적인 HttpStatus 처리
    public static <T> ResponseDTO<T> ok(String message, T data) {
        return new ResponseDTO<>(HttpStatus.OK.value(), message, data);
    }
    public static <T> ResponseDTO<T> created(String message, T data) {
        return new ResponseDTO<>(HttpStatus.CREATED.value(), message, data);
    }
    public static <T> ResponseDTO<T> badRequest(String message) {
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), message, null);
    }
    public static <T> ResponseDTO<T> notFound(String message) {
        return new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), message, null);
    }
    public static <T> ResponseDTO<T> internalServerError(String message) {
        return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    // 사용자 정의 HttpStatus 처리
    public static <T> ResponseDTO<T> custom(HttpStatus status, String message, T data) {
        return new ResponseDTO<>(status.value(), message, data);
    }
}