package org.artisan.api;

import lombok.RequiredArgsConstructor;
import org.artisan.attributes.Auth;
import org.artisan.attributes.MemberOnly;
import org.artisan.core.User;
import org.artisan.payload.PreferenceCategoriesResponse;
import org.artisan.payload.PreferenceUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preferences")
public class PreferenceApi {

    @MemberOnly
    public PreferenceCategoriesResponse find(@Auth User user) {
        return null;
    }

    @MemberOnly
    public ResponseEntity<Void> update(@Auth User requester, PreferenceUpdateRequest request) {

        return ResponseEntity.noContent().build();
    }
}

