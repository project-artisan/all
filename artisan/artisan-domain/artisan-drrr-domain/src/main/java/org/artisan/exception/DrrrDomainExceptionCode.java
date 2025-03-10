package org.artisan.exception;

import lombok.Getter;
import org.artsian.web.core.exception.HttpExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum DrrrDomainExceptionCode implements HttpExceptionCode {

    NOT_FOUND_TECHBLOG_POST("A01", "블로그가 존재하지 않습니다.");

    // CoreApi
    private static final String PREFIX = "CA";

    private final HttpStatus httpStatus;
    private final String identifier;
    private final String message;
    private final LogLevel level;

    DrrrDomainExceptionCode(String identifier, String message) {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.identifier = identifier;
        this.message = message;
        this.level = LogLevel.WARN;
    }


    // DRRR-DOMAIN
    @Override
    public String getPrefix() {
        return "DD";
    }

}
