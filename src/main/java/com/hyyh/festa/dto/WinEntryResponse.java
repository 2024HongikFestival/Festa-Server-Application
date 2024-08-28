package com.hyyh.festa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinEntryResponse {
    private Long entryId;
    private String name;
    private String phone;
    private String prize;
    private String comment;
    private int date;
    private boolean isWinner;
    private boolean duplicate;
}
