package com.hyyh.festa.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackListResponseDTO {
    private String userId;
    private LocalDateTime blockedAt;
}
