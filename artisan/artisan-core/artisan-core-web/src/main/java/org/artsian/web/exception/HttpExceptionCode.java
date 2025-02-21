package org.artsian.web.exception;

import org.artisan.core.CoreExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public interface HttpExceptionCode extends CoreExceptionCode {

    HttpStatus getHttpStatus();
    LogLevel getLevel();

}