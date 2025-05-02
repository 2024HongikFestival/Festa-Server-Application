package com.hyyh.festa.oidc;

import lombok.Getter;

@Getter
public class KakaoErrorException extends RuntimeException {
    private final String errorCode;

    public KakaoErrorException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
