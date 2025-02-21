package org.artsian.web.exception;

import org.artisan.core.CoreException;
import org.artisan.core.CoreExceptionCode;

public class InternalServerError extends CoreException {

    public InternalServerError(HttpExceptionCode code) {
        super(code);
    }
}
