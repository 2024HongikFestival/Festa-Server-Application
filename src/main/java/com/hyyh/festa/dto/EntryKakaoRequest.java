package com.hyyh.festa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryKakaoRequest {

    private String code;
    private double latitude;
    private double longitude;
}
