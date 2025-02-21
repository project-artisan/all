package org.artsian.web.exception;

import org.artisan.core.CoreException;

public class BadRequestException extends CoreException {

    public BadRequestException(HttpExceptionCode code) {
        super(code);
    }

}
