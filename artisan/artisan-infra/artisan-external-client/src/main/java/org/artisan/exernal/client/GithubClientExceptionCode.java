package org.artisan.exernal.client;

import lombok.RequiredArgsConstructor;
import org.artsian.web.core.exception.HttpExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GithubClientExceptionCode implements HttpExceptionCode {
    INVALID_OAUTH_CODE("A01", HttpStatus.BAD_REQUEST , LogLevel.WARN, "유효하지 않은 OAUTH 요청입니다."),
    INVALID_OAUTH_SERVER("A02", HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR,  "현재 서버 연결이 불안정합니다.")
    ;

    // ExternalClient
    private static final String PREFIX= "EXC";
    private final String identifier;
    private final HttpStatus httpStatus;
    private final LogLevel logLevel;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public LogLevel getLevel() {
        return logLevel;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
