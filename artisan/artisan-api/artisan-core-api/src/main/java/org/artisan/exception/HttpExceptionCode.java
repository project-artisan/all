package org.artisan.exception;

import org.artisan.core.CoreExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public interface HttpExceptionCode extends CoreExceptionCode {

    HttpStatus getHttpStatus();
    LogLevel getLevel();

}
