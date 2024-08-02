package com.hyyh.festa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    public void updateEvent(String title, String prize, String requires,
                            LocalDateTime startAt, LocalDateTime endAt, LocalDateTime announcedAt, String imageUrl){
        this.title = title;
        this.prize = prize;
        this.requires = requires;
        this.startAt = startAt;
        this.endAt = endAt;
        this.announcedAt = announcedAt;
        this.imageUrl = imageUrl;
    };
}