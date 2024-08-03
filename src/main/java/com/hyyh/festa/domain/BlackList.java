package com.hyyh.festa.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "black_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "festa_user_id")
    private FestaUser festaUser;
}
