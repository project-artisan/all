package org.artisan.exception;

import lombok.Getter;
import org.artsian.web.core.exception.HttpExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum InterviewDomainExceptionCode implements HttpExceptionCode {

    NOT_FOUND_INTERVIEW("A01", "인터뷰가 존재하지 않습니다."),
    INTERVIEW_IS_DONE("A02","완료된 인터뷰입니다."),
    FAIL_INTERVIEW_CREATE("A03", "인터뷰 생성을 실패했습니다"),
    FAIL_SUBMIT_ANSWER_TO_IQ("A04", " 현재 인터뷰 질문에 답변을 추가할 수 없습니다"),
    NOT_FOUND_INTERVIEW_QUESTION("B01", "인터뷰 질문이 존재하지 않습니다."),
    NOT_FOUND_QUESTION_SET("C01", "질문 목록을 찾을 수 없습니다."),
    NOT_FOUND_TAIL_QUESTION("D01", "꼬리 질문을 찾을 수 없습니다.");

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
