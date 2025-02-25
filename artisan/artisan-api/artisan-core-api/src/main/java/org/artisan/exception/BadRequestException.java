package org.artisan.exception;

import org.artisan.core.exception.CoreException;

public class BadRequestException extends CoreException {
    public BadRequestException(CoreApiExceptionCode code) {
        super(code);
    }
}
