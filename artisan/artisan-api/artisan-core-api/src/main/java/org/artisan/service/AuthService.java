package org.artisan.service;

import java.time.Instant;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.api.payload.request.OAuthProfileRequest;
import org.artisan.api.payload.request.SigninRequest;
import org.artisan.api.payload.request.SignupRequest;
import org.artisan.api.payload.response.OAuthProfileResponse;
import org.artisan.core.User;
import org.artisan.domain.Member;
import org.artisan.exernal.client.GithubApiClient;
import org.artisan.provider.JwtProvider;
import org.artisan.provider.JwtProvider.AccessTokenClaims;
import org.artisan.provider.JwtProvider.RefreshTokenClaims;
import org.artisan.service.dto.IssuedToken;
import org.artisan.service.v1.MemberServiceImpl;
import org.artisan.service.v1.MemberTokenServiceImpl;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final GithubApiClient githubApiClient;
    private final MemberServiceImpl memberService;
    private final JwtProvider jwtProvider;
    private final MemberTokenServiceImpl memberTokenService;

    public OAuthProfileResponse findProfile(OAuthProfileRequest request) {
        var profile = githubApiClient.requestProfile(request.providerId(), request.redirectUrl());

        return new OAuthProfileResponse(
                profile.id(),
                profile.avatarUrl(),
                memberService.isMember(request.toCredentials())
        );
    }

    public IssuedToken signup(SignupRequest request) {
        var member = memberService.create(request.toProfile(), request.toCredentials());

        return issueToken(member);
    }

    public IssuedToken signin(SigninRequest signinRequest) {
        var member = memberService.read(signinRequest.toCredentials());

        return issueToken(member);
    }

    public IssuedToken reIssue(String refreshToken) {
        var now = Instant.now();
        var refreshTokenClaims = jwtProvider.decodeRefreshToken(refreshToken);
        var memberToken = memberTokenService.read(refreshTokenClaims.memberId());
        var member = memberToken.getMember();

        if (memberToken.shouldRefresh(LocalDateTime.now())) {
            log.info("refresh token renewal [member id={}]", member.getId());
            memberTokenService.delete(memberToken);
            return issueToken(member);
        }

        var accessTokenClaims = AccessTokenClaims.from(member.getId());
        var accessToken = jwtProvider.createAccessToken(accessTokenClaims, now);

        return new IssuedToken(
                accessToken,
                refreshToken
        );
    }


    private IssuedToken issueToken(Member member) {
        var nowInstant = Instant.now();
        var nowLocalDateTime = LocalDateTime.now();

        var token = memberTokenService.write(member, nowLocalDateTime.plusWeeks(1));

        var accessTokenClaims = new AccessTokenClaims(member.getId());
        var refreshTokenClaims = RefreshTokenClaims.of(member.getId(), token.getId());

        var accessToken = jwtProvider.createAccessToken(accessTokenClaims, nowInstant);
        var refreshToken = jwtProvider.createRefreshToken(refreshTokenClaims, nowInstant);

        return new IssuedToken(
                accessToken,
                refreshToken
        );
    }


    public void logout(User user, String refreshToken ) {
        var refreshTokenClaims = jwtProvider.decodeRefreshToken(refreshToken);

        memberTokenService.delete(refreshTokenClaims.tokenId());

    }
}
