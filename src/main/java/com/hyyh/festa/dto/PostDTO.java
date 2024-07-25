package com.hyyh.festa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDTO {

    private String foundLocation;
    private String storageLocation;
    private String content;
    private String photoUrl;
}
