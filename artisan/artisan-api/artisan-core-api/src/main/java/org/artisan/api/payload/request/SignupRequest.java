package org.artisan.api.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record SignupRequest(
        @NotNull
        String providerId,
        @NotNull
        String avatarUrl,

        @Length(min = 3, max = 20)
        @NotNull
        @NotBlank
        String nickname
) {

}
