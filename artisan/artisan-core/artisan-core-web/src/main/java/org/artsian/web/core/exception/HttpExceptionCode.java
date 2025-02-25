package org.artsian.web.core.exception;

import org.artisan.core.exception.CoreExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public interface HttpExceptionCode extends CoreExceptionCode {

    HttpStatus getHttpStatus();
    LogLevel getLevel();

}