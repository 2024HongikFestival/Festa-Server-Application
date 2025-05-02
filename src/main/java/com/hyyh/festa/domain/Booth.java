package com.hyyh.festa.domain;

import jakarta.persistence.*;
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

    private int previousLike = 0;

    @Enumerated(EnumType.STRING)
    private BoothPart boothPart;

    public void plusLikeCount() {
        this.totalLike += 1;
    }
}
