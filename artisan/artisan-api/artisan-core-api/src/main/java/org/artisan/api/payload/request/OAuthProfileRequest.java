package org.artisan.api.payload.request;

import org.artisan.core.OAuth2State;
import org.artisan.domain.member.OAuthExternalCredentials;

public record OAuthProfileRequest(
        OAuth2State provider,
        String code,
        String redirectUrl
) {

    public OAuthExternalCredentials toCredentials(String providerId){
        return OAuthExternalCredentials.of(provider, providerId);
    }
}
