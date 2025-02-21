package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.OAuth2State;
import org.artisan.core.domain.BaseEntity;
import org.artisan.domain.member.MemberProfile;
import org.artisan.domain.member.OAuthExternalCredentials;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {


    @Embedded
    private MemberProfile profile;

    @Embedded
    private OAuthExternalCredentials credentials;


    public Member(
            MemberProfile profile,
            OAuthExternalCredentials credentials
    ) {
        this.profile = profile;
        this.credentials = credentials;
    }
}
