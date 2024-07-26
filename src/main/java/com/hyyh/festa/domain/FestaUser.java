package com.hyyh.festa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FestaUser implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;

    private String kakaoSub;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return "USER";
                    }
                }
        );
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return kakaoSub;
    }
}
