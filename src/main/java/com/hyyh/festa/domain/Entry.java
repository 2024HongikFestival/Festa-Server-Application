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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "event_id", nullable = true)
    @Setter
    private Event event;

    @ManyToOne
    private FestaUser user;

    @NotNull
    private String name;
    @NotNull
    private String phone;
    private String comment;
}
