package org.artisan.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.artisan.api.payload.request.OAuthProfileRequest;
import org.artisan.api.payload.request.SigninRequest;
import org.artisan.api.payload.request.SignupRequest;
import org.artisan.api.payload.response.AccessTokenResponse;
import org.artisan.api.payload.response.OAuthProfileResponse;
import org.artisan.attributes.Auth;
import org.artisan.core.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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


    @PostMapping("/sign-up")
    public ResponseEntity<AccessTokenResponse> signup(SignupRequest signupRequest, HttpServletResponse response){
        return null;
    }

    @PostMapping("sign-in")
    public ResponseEntity<AccessTokenResponse> signin(SigninRequest signinRequest, HttpServletResponse response){
        return null;
    }

    @GetMapping("/oauth/profile")
    public OAuthProfileResponse getProfile(@RequestBody OAuthProfileRequest profile) {
        return null;
    }


    @PostMapping("/token/reissue")
    public ResponseEntity<AccessTokenResponse> reIssue(@CookieValue("refreshToken") String refreshToken) {
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Auth  User visitor) {
        return null;
    }

}
