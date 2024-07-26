package com.hyyh.festa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class TokenResponse {
    private Integer status;
    private String message;
    private List<?> data;

    public TokenResponse(String token) {
        this.status = 200;
        this.message = "어드민 인가 토큰 발급";
        this.data = List.of(new inner(token));
    }

    @Data
    @AllArgsConstructor
    public class inner {
        private String accessToken;
    }
}
