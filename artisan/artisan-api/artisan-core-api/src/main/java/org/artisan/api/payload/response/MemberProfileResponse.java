package org.artisan.api.payload.response;

import lombok.Builder;

@Builder
public record MemberProfileResponse(
        Long id,
        String nickname,
        String email,
        String avatar
) {

}
