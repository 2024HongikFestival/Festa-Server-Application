package com.hyyh.festa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @NotNull
    private String title;
    @NotNull
    private String prize;
    private String requires;

    @NotNull
    private LocalDateTime startAt;
    @NotNull
    private LocalDateTime endAt;
    @NotNull
    private LocalDateTime announcedAt;

    private String imageUrl;
}