package com.hyyh.festa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class KakaoPublicKey {

    @Id
    @GeneratedValue
    private Long id;

    private String kid;
    @Column(length = 800)
    private String n;
    private String e;
    private LocalDateTime lastUpdated;

}
