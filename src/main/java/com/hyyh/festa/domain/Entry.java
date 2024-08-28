package com.hyyh.festa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Entry {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private FestaUser user;

    @NotNull
    private String name;

    @NotNull
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Prize prize;

    @NotNull
    private String comment;

    @Setter
    private int date;

    @Setter
    private boolean isWinner;
}