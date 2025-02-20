package org.artisan.api;


import lombok.RequiredArgsConstructor;
import org.artisan.api.payload.response.MemberProfileResponse;
import org.artisan.attributes.Auth;
import org.artisan.attributes.MemberOnly;
import org.artisan.core.User;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApi {


    @MemberOnly
    public MemberProfileResponse getMe(@Auth User user) {
        return null;
    }
}

