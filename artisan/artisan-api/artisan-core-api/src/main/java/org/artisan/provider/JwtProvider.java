package org.artisan.provider;


import static org.artisan.exception.CoreApiExceptionCode.EXPIRED_ACCESS_TOKEN;
import static org.artisan.exception.CoreApiExceptionCode.EXPIRED_REFRESH_TOKEN;
import static org.artisan.exception.CoreApiExceptionCode.INVALID_ACCESS_TOKEN;
import static org.artisan.exception.CoreApiExceptionCode.INVALID_REFRESH_TOKEN;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.exception.InvalidJwtException;
import org.artisan.exception.JwtExpiredException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtProvider {
    private static final String ISSUER = "ARTISAN";
    public static final int accessTokenExpiry = 24 * 60 * 60; // 1일
    public static final int refreshTokenExpiry = 7 * 24 * 60 * 60; // 1주
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String createAccessToken(
            AccessTokenClaims accessTokenClaims,
            Instant issuanceTime
    ) {
        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(issuanceTime)
                .expiresAt(issuanceTime.plusSeconds(accessTokenExpiry))
                .subject("access_token")
                .claims((map) -> map.put("member_id", accessTokenClaims.memberId()))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String createRefreshToken(
            RefreshTokenClaims refreshTokenClaims,
            Instant issuanceTime
    ) {
        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(issuanceTime)
                .expiresAt(issuanceTime.plusSeconds(refreshTokenExpiry))
                .claims(map -> {
                    map.put("member_id", refreshTokenClaims.memberId());
                    map.put("token_id", refreshTokenClaims.tokenId());
                })
                .subject("refresh_token")
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


    private Map<String, Object> decode(final String token) {
        return jwtDecoder.decode(token).getClaims();
    }

    public AccessTokenClaims decodeAccessToken(final String accessToken) {
        try {

            var claimsMap = this.decode(accessToken);

            return new AccessTokenClaims(
                    Long.parseLong(String.valueOf(claimsMap.get("member_id")))

            );
        } catch (JwtValidationException exception) {
            throw new JwtExpiredException(EXPIRED_ACCESS_TOKEN);
        } catch (JwtException exception) {
            throw new InvalidJwtException(INVALID_ACCESS_TOKEN);
        }
    }

    public RefreshTokenClaims decodeRefreshToken(final String refreshToken) {
        try {
            var jwt = jwtDecoder.decode(refreshToken);
            var claimsMap = jwt.getClaims();

            return new RefreshTokenClaims(
                    Long.parseLong(String.valueOf(claimsMap.get("member_id"))),
                    Long.parseLong(String.valueOf(claimsMap.get("token_id"))),
                    jwt.getExpiresAt()
            );
        } catch (JwtValidationException exception) {
            throw new JwtExpiredException(EXPIRED_REFRESH_TOKEN);
        } catch (JwtException exception) {
            log.error(exception.getMessage());
            throw new InvalidJwtException(INVALID_REFRESH_TOKEN);
        }
    }

    public String extractToken(final HttpServletRequest request) {
        final var token = request.getHeader(HEADER_AUTHORIZATION);

        return extractToken(token);
    }

    public String extractToken(final String headerValue) {
        if (!Objects.isNull(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public record AccessTokenClaims(
            Long memberId
    ) {

        public static AccessTokenClaims from(Long memberId){
            return new AccessTokenClaims(memberId);
        }
    }

    public record RefreshTokenClaims(
            Long memberId,
            Long tokenId,
            Instant expiredAt
    ) {

        public static RefreshTokenClaims of(Long memberId, Long tokenId) {
            return new RefreshTokenClaims(memberId, tokenId, null);
        }
    }

}
