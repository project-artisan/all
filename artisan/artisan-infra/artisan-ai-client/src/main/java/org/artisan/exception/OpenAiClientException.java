package org.artisan.exception;

import org.artisan.core.exception.CoreException;
import org.artisan.core.exception.CoreExceptionCode;
import org.artsian.web.core.exception.HttpExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class OpenAiClientException extends CoreException  {

    public OpenAiClientException() {
        super(new HttpExceptionCode() {

            @Override
            public String getPrefix() {
                return "AC";
            }

            @Override
            public HttpStatus getHttpStatus() {
                return HttpStatus.BAD_REQUEST;
            }

            @Override
            public LogLevel getLevel() {
                return LogLevel.WARN;
            }

            @Override
            public String getIdentifier() {
                return "A01";
            }

            @Override
            public String getMessage() {
                return "Open AI 서버가 불안정합니다.";
            }

        });
    }
}
