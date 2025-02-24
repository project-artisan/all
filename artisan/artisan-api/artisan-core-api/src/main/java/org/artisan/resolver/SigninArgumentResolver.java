package org.artisan.resolver;

import static org.artisan.exception.CoreApiExceptionCode.INVALID_REFRESH_TOKEN;
import static org.artisan.exception.CoreApiExceptionCode.NOT_FOUND_REFRESH_TOKEN;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_REQUEST;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.attributes.Auth;
import org.artisan.core.User;
import org.artisan.exception.BadRequestException;
import org.artisan.exception.RefreshTokenException;
import org.artisan.provider.CookieProvider;
import org.artisan.provider.JwtProvider;
import org.artisan.provider.JwtProvider.RefreshTokenClaims;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class SigninArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.withContainingClass(User.class)
                .hasParameterAnnotation(Auth.class);
    }

    @Override
    public User resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {

        final var httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        return extractMemberId(httpServletRequest.getCookies())
                .filter(memberIdToRefreshToken -> {
                    var accessTokenClaims = jwtProvider.decodeAccessToken(jwtProvider.extractToken(httpServletRequest));
                    return Objects.equals(accessTokenClaims.memberId(), memberIdToRefreshToken);
                })
                .map(User::member)
                .orElseGet(User::guest);
    }
    private Optional<Long> extractMemberId(Cookie... cookies) {
        try {
            if (Objects.isNull(cookies)) {
                throw new RefreshTokenException(NOT_FOUND_REFRESH_TOKEN);
            }
            return Optional.of(Arrays.stream(cookies)
                    .filter(cookie -> CookieProvider.REFRESH_TOKEN_KEY.equals(cookie.getName()))
                            .peek(r -> log.info("{}", r))
                    .map(Cookie::getValue)
                    .map(jwtProvider::decodeRefreshToken)
                    .map(RefreshTokenClaims::memberId)
                    .findFirst()
                    .orElseThrow(() -> new RefreshTokenException(NOT_FOUND_REFRESH_TOKEN)));
        } catch (RefreshTokenException refreshTokenException) {
            return Optional.empty();
        }


    }
}
