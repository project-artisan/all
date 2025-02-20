package org.artisan.exception;

import org.artisan.core.CoreException;

public class BadRequestException extends CoreException {
    public BadRequestException(CoreApiExceptionCode code) {
        super(code);
    }
}
