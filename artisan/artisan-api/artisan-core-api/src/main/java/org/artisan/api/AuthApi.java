package org.artisan.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.artisan.api.payload.request.OAuthProfileRequest;
import org.artisan.api.payload.request.SigninRequest;
import org.artisan.api.payload.request.SignupRequest;
import org.artisan.api.payload.response.AccessTokenResponse;
import org.artisan.api.payload.response.OAuthProfileResponse;
import org.artisan.attributes.Auth;
import org.artisan.attributes.MemberOnly;
import org.artisan.core.User;
import org.artisan.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApi {

    private final AuthService authService;


    @PostMapping("/sign-up")
    public ResponseEntity<AccessTokenResponse> signup(SignupRequest request, HttpServletResponse response){
        var issuedToken = this.authService.signup(request);

        return ResponseEntity.ok(new AccessTokenResponse(issuedToken.accessToken()));
    }

    @PostMapping("sign-in")
    public ResponseEntity<AccessTokenResponse> signin(SigninRequest request, HttpServletResponse response){
        var issuedToken = this.authService.signin(request);

        return ResponseEntity.ok(new AccessTokenResponse(issuedToken.accessToken()));
    }

    @GetMapping("/oauth/profile")
    public OAuthProfileResponse getProfile(@RequestBody OAuthProfileRequest request) {
        return this.authService.findProfile(request);
    }


    @PostMapping("/token/reissue")
    public ResponseEntity<AccessTokenResponse> reIssue(
            @CookieValue("refreshToken") String refreshToken
    ) {
        var issuedToken = this.authService.reIssue(refreshToken);

        return ResponseEntity.ok(new AccessTokenResponse(issuedToken.accessToken()));
    }

    @MemberOnly
    @PostMapping("/logout")
    public void logout(
            @Auth User user,
            @CookieValue("refreshToken") String refreshToken
    ) {
        authService.logout(user, refreshToken);
    }

}
