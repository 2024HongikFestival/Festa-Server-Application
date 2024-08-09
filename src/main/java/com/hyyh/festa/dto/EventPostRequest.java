package com.hyyh.festa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventPostRequest {
    @NotBlank(message = "이벤트 제목은 반드시 필요합니다.")
    @Size(max = 50, message = "제목은 50글자까지 입력할 수 있습니다.")
    private String title;

    @NotBlank(message = "상품은 반드시 필요합니다.")
    @Size(max = 100, message = "상품은 100글자까지 입력할 수 있습니다.")
    private String prize;

    @NotBlank(message = "조건은 반드시 필요합니다.")
    @Size(max = 300, message = "조건은 300글자까지 입력할 수 있습니다.")
    private String requires;

    @NotNull(message = "시작일은 반드시 필요합니다.")
    private LocalDateTime startAt;

    @NotNull(message = "종료일은 반드시 필요합니다.")
    private LocalDateTime endAt;

    @NotNull(message = "발표일은 반드시 필요합니다.")
    private LocalDateTime announcedAt;

    private String imageUrl;
}