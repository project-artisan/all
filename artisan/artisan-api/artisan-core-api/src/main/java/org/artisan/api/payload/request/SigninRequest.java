package org.artisan.api.payload.request;

import org.artisan.core.OAuth2State;
import org.artisan.domain.member.OAuthExternalCredentials;

public record SigninRequest(
        OAuth2State provider,
        String providerId
) {

    public OAuthExternalCredentials toCredentials(){
        return OAuthExternalCredentials.of(provider, providerId);
    }
}
