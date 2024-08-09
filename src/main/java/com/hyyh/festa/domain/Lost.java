package com.hyyh.festa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Lost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festa_user_id")
    private FestaUser festaUser;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LostStatus lostStatus;

    @NotNull
    private String foundLocation;

    @NotNull
    private String storageLocation;

    private String content;

    @NotNull
    private String imageUrl;

    @CreatedDate
    private LocalDateTime createdAt;
}
