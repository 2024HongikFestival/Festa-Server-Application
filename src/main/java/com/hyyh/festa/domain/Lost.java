package com.hyyh.festa.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Lost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festa_user_id")
    private FestaUser festaUser;

    private String foundLocation; //발견 장소
    private String storageLocation; //보관 장소
    private String content; //본문
    private String imageUrl; //사진 url

    @CreatedDate
    private LocalDateTime createdAt; //작성 시간
}
