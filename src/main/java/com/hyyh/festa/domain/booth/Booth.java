package com.hyyh.festa.domain.booth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Booth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boothName;

    private int likeCount;

    @Builder
    public Booth(String boothName, int likeCount) {
        this.boothName = boothName;
        this.likeCount = likeCount;
    }

    public void plusLikeCount() {
        this.likeCount += 1;
    }
}
