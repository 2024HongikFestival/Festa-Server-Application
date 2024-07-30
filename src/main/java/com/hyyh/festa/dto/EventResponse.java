package com.hyyh.festa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private Long eventId;
    private String title;
    private String prize;
    private String requires;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime announcedAt;
    private String imageUrl;
}