package org.artisan.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.artsian.web.exception.HttpExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CoreApiExceptionCode implements HttpExceptionCode {
    UNAUTHORIZED_MEMBER(HttpStatus.FORBIDDEN, "A01", "권한 없는 사용자입니다", LogLevel.WARN),

    NOT_FOUND_ACCESS_TOKEN("A02", "access token이 존재하지 않습니다."),
    NOT_FOUND_REFRESH_TOKEN("A03", "refresh token이 존재하지 않습니다."),

    EXPIRED_ACCESS_TOKEN("A04", "access token 만료됐습니다."),
    EXPIRED_REFRESH_TOKEN("A05", "refresh token 만료됐습니다."),

    INVALID_ACCESS_TOKEN("A06", "유효하지 않은 access token 입니다."),
    INVALID_REFRESH_TOKEN("A07", "유효하지 않은 refresh token 입니다."),
    INVALID_OAUTH_CODE("A08", "유효하지 않은 OAUTH 요청입니다."),
    INVALID_OAUTH_SERVER("A09", "현재 서버 연결이 불안정합니다."),
    ;

    // CoreApi
    private static final String PREFIX = "CA";

    private final HttpStatus httpStatus;
    private final String identifier;
    private final String message;
    private final LogLevel level;

    CoreApiExceptionCode(String identifier, String message) {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.identifier = identifier;
        this.message = message;
        this.level = LogLevel.WARN;
    }

    @Override
    public String getPrefix() {
        return "CA";
    }
}
