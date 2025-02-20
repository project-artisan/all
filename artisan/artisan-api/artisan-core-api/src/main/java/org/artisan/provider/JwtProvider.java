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
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtProvider {
    private static final String issuer = "MUBAEGSEU";
    private static final Long accessTokenExpiry = 24 * 60 * 60L; // 1일
    private static final Long refreshTokenExpiry = 7 * 24 * 60 * 60L; // 1주
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String createAccessToken(final Long id, final Instant issuanceTime) {
        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(issuanceTime)
                .expiresAt(issuanceTime.plusSeconds(accessTokenExpiry))
                .subject(String.valueOf(id))
                .claim("id", id)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String createRefreshToken(final Long id, final Instant issuanceTime) {
        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(issuanceTime)
                .expiresAt(issuanceTime.plusSeconds(refreshTokenExpiry))
                .subject(String.valueOf(id))
                .claim("id", id)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private Map<String, Object> decode(final String token) {
        return jwtDecoder.decode(token).getClaims();
    }

    public Long decodeAccessToken(final String accessToken) {
        try {
            return Long.parseLong(String.valueOf(this.decode(accessToken).get("id")));
        } catch (JwtValidationException exception) {
            throw new JwtExpiredException(EXPIRED_ACCESS_TOKEN);
        } catch (Exception exception) {
            throw new InvalidJwtException(INVALID_ACCESS_TOKEN);
        }
    }

    public Long decodeRefreshToken(final String refreshToken) {
        try {
            return Long.parseLong(String.valueOf(this.decode(refreshToken).get("id")));
        } catch (JwtValidationException exception) {
            throw new JwtExpiredException(EXPIRED_REFRESH_TOKEN);
        } catch (Exception exception) {
            throw new InvalidJwtException(INVALID_REFRESH_TOKEN);
        }
    }

    public String extractToken(final HttpServletRequest request) {
        final String token = request.getHeader(HEADER_AUTHORIZATION);

        return extractToken(token);

    }

    public String extractToken(final String headerValue) {
        if (!Objects.isNull(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public void validRefreshToken(String refreshToken) {
        decodeRefreshToken(refreshToken);
    }
}
