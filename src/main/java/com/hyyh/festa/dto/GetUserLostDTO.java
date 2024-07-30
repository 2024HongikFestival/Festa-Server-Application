package com.hyyh.festa.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
@Builder
public class GetUserLostDTO {

    private long lostId;
    private String foundLocation;
    private String storageLocation;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
}
