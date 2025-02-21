package org.artisan.api.payload.response;

import lombok.Builder;

@Builder
public record OAuthProfileResponse(
        String providerId,
        String avatarUrl,
        boolean isRegistered
) {
    public static OAuthProfileResponse from(OAuthProfileResponse profile) {
        return null;
    }
}
