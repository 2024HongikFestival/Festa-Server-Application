package com.hyyh.festa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
public class getUserLostDTO {

    private long lostId;
    private String foundLocation;
    private String storageLocation;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;

}
