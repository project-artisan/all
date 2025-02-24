package org.artisan.api.payload.response;

import lombok.Builder;
import org.artisan.domain.Member;

@Builder
public record MemberProfileResponse(
        Long id,
        String nickname,
        String avatar
) {

    public static MemberProfileResponse from(Member member) {
        return new MemberProfileResponse(
                member.getId(),
                member.getProfile().nickname(),
                member.getProfile().profileImage().toUrl()
        );
    }
}
