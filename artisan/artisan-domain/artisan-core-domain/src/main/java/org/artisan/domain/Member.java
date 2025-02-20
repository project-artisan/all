package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.domain.BaseEntity;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column(nullable = false)
    private String nickname;

    private String provider;
    @Column(unique = true)
    private String providerId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String profileImageUrl;

    @Column(nullable = false)
    private boolean isActive;


    @Builder
    public Member(String nickname, String provider, String providerId, String email, String profileImageUrl) {
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }
}
