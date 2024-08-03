package com.hyyh.festa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryResponse {
    private Long entryId;
    private Long eventId;
    private String name;
    private String phone;
    private String comment;
}
