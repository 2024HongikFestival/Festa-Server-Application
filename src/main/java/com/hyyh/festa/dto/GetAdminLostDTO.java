package com.hyyh.festa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hyyh.festa.domain.LostStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
@Builder
public class GetAdminLostDTO {

    private Long lostId;
    private LostStatus lostStatus;
    private String userId;
    @JsonProperty("isUserBlocked")
    private boolean isUserBlocked;
    private String foundLocation;
    private String storageLocation;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;

}