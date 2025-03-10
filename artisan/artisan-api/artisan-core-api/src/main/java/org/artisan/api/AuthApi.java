package org.artisan.api;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import jakarta.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.api.payload.request.OAuthProfileRequest;
import org.artisan.api.payload.request.SigninRequest;
import org.artisan.api.payload.request.SignupRequest;
import org.artisan.api.payload.response.AccessTokenResponse;
import org.artisan.api.payload.response.OAuthProfileResponse;
import org.artisan.attributes.Auth;
import org.artisan.attributes.MemberOnly;
import org.artisan.core.User;
import org.artisan.provider.CookieProvider;
import org.artisan.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApi {

    private final AuthService authService;
    private final CookieProvider cookieProvider;


    @PostMapping("/sign-up")
    public ResponseEntity<AccessTokenResponse> signup(@RequestBody SignupRequest request, HttpServletResponse response){
        var issuedToken = this.authService.signup(request);

        response.addHeader(SET_COOKIE, cookieProvider.createCookie(issuedToken.refreshToken()).toString());
        return ResponseEntity.ok(new AccessTokenResponse(issuedToken.accessToken()));
    }

    // TODO 로그인 로직 개선하기
    @PostMapping("sign-in")
    public ResponseEntity<AccessTokenResponse> signin(@RequestBody SigninRequest request, HttpServletResponse response){
        var issuedToken = this.authService.signin(request);

        response.addHeader(SET_COOKIE, cookieProvider.createCookie(issuedToken.refreshToken()).toString());
        return ResponseEntity.ok(new AccessTokenResponse(issuedToken.accessToken()));
    }

    @GetMapping("/oauth/profile")
    public OAuthProfileResponse getProfile(@ModelAttribute  OAuthProfileRequest request) {
        log.info("{}", request);
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
            HttpServletResponse response,
            @CookieValue("refreshToken") String refreshToken
    ) {

        authService.logout(user, refreshToken);
        response.addHeader(SET_COOKIE, cookieProvider.expireCookie().toString());
    }

}
