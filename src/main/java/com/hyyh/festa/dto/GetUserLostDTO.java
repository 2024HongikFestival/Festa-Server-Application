package com.hyyh.festa.dto;

import com.hyyh.festa.domain.LostStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
@Builder
public class GetUserLostDTO {

    private Long lostId;
    private LostStatus lostStatus;
    private String foundLocation;
    private String storageLocation;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
}
