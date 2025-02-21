package org.artisan.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.domain.BaseEntity;
import org.artisan.domain.token.TokenExpiration;

@Getter
@Entity
@Table(name = "member_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberToken extends BaseEntity {

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Embedded
    private TokenExpiration expiration;


    public MemberToken(Member member, TokenExpiration expiration) {
        this.member = member;
        this.expiration = expiration;
    }

    public static MemberToken from(Member member, LocalDateTime expiredAt){
        return new MemberToken(member, TokenExpiration.from(expiredAt));
    }

    public MemberToken(Member member) {
        this.member = member;
    }

    public void updateExpiredAt(LocalDateTime expiredAt) {
        this.expiration = TokenExpiration.from(expiredAt);
    }

    // TODO 토큰 갱신 처리 기능 추가 예정
    public boolean shouldRefresh(LocalDateTime now) {
        return false;
    }

    public void expire() {
        this.expiration = expiration.delete();
    }
}