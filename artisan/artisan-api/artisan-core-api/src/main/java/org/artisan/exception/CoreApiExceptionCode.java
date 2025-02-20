package org.artisan.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.artisan.core.CoreExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum CoreApiExceptionCode implements HttpExceptionCode{
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "A01", "권한 없는 사용자입니다", LogLevel.WARN)
    ;

    // CoreApi
    private static final String PREFIX = "CA";

    private final HttpStatus httpStatus;
    private final String identifier;
    private final String message;
    private final LogLevel level;


    @Override
    public String getPrefix() {
        return "CA";
    }
}
