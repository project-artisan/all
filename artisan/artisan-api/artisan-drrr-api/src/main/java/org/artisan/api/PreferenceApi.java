package org.artisan.api;

import lombok.RequiredArgsConstructor;
import org.artisan.attributes.Auth;
import org.artisan.attributes.MemberOnly;
import org.artisan.core.User;
import org.artisan.payload.PreferenceCategoriesResponse;
import org.artisan.payload.PreferenceUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preferences")
public class PreferenceApi {

    @MemberOnly
    @GetMapping
    public PreferenceCategoriesResponse find(@Auth User user) {
        return null;
    }

    @MemberOnly
    @PutMapping()
    public ResponseEntity<Void> update(
            @Auth User requester,
            @RequestBody PreferenceUpdateRequest request
    ) {

        return ResponseEntity.noContent().build();
    }
}

