package com.hyyh.festa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class JwtBlacklist {
    @Id
    @GeneratedValue
    private Long id;

    private String token;
}