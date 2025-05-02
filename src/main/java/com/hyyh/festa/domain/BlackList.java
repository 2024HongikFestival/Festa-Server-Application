package com.hyyh.festa.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "black_id")
    private Long id;

    @CreatedDate
    private LocalDateTime blockedAt;

    @ManyToOne
    @JoinColumn(name = "festa_user_id")
    private FestaUser festaUser;
}
