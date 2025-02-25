package org.artsian.web.core.exception;

import org.artisan.core.exception.CoreException;

public class BadRequestException extends CoreException {

    public BadRequestException(HttpExceptionCode code) {
        super(code);
    }

}
