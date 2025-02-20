package org.artisan.api.payload.request;

public record OAuthProfileRequest(
        String providerId,
        String redirectUrl
) {
}
