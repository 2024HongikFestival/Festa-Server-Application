package com.hyyh.festa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Booth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boothName;

    private int totalLike;

    private int previoudLike = 0;

    @Builder
    public Booth(String boothName, int totalLike) {
        this.boothName = boothName;
        this.totalLike = totalLike;
    }

    public void plusLikeCount() {
        this.totalLike += 1;
    }
}
