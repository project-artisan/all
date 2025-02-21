package org.artisan.api.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.artisan.core.OAuth2State;
import org.artisan.domain.file.ExternalURL;
import org.artisan.domain.member.MemberProfile;
import org.artisan.domain.member.OAuthExternalCredentials;
import org.hibernate.validator.constraints.Length;

@Builder
public record SignupRequest(
        @NotNull
        OAuth2State provider,

        @NotNull
        String providerId,
        @NotNull
        String avatarUrl,

        @Length(min = 3, max = 20)
        @NotNull
        @NotBlank
        String nickname
) {

    public OAuthExternalCredentials toCredentials(){
        return new OAuthExternalCredentials(provider, providerId);
    }

    public MemberProfile toProfile(){
        return new MemberProfile(nickname, ExternalURL.from(avatarUrl));
    }
}
