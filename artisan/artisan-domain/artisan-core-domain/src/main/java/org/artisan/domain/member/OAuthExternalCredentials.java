package org.artisan.domain.member;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.artisan.core.OAuth2State;

// 두 값을 합친게 유니크 해야 함
@Embeddable
public record OAuthExternalCredentials(
        @Enumerated(EnumType.STRING) OAuth2State provider,
        String providerId
){

    public static OAuthExternalCredentials of(OAuth2State provider, String providerId) {
        return new OAuthExternalCredentials(provider, providerId);
    }
}
