package org.artsian.web.core.exception;

import org.artisan.core.exception.CoreException;

public class InternalServerError extends CoreException {

    public InternalServerError(HttpExceptionCode code) {
        super(code);
    }
}
