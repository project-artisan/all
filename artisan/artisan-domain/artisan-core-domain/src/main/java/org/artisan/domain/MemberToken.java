package org.artisan.domain;

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

@Getter
@Entity
@Table(name = "member_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberToken extends BaseEntity {

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private LocalDateTime expiredAt;


    public MemberToken(Member member) {
        this.member = member;
    }

    public void updateExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public boolean shouldRefresh(LocalDateTime now) {
        return expiredAt.minusWeeks(1)
                .isBefore(now);
    }

}