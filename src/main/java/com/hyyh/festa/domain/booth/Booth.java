package com.hyyh.festa.domain.booth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Builder
    public Booth(String boothName, int totalLike) {
        this.boothName = boothName;
        this.totalLike = totalLike;
    }

    public void plusLikeCount() {
        this.totalLike += 1;
    }
}
