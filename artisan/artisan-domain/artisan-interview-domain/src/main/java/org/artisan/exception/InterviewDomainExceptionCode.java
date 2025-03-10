package org.artisan.exception;

import lombok.Getter;
import org.artsian.web.core.exception.HttpExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum InterviewDomainExceptionCode implements HttpExceptionCode {

    NOT_FOUND_INTERVIEW("A01", "인터뷰가 존재하지 않습니다."),
    INTERVIEW_IS_DONE("A02","완료된 인터뷰입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String identifier;
    private final String message;
    private final LogLevel level;

    InterviewDomainExceptionCode(String identifier, String message) {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.identifier = identifier;
        this.message = message;
        this.level = LogLevel.WARN;
    }


    // INTERVIEW DOMAIN
    @Override
    public String getPrefix() {
        return "ID";
    }

}
