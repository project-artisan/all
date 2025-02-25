package org.artisan.exception;

import org.artisan.core.exception.CoreException;

public class JwtExpiredException extends CoreException {

    public JwtExpiredException(CoreApiExceptionCode code) {
        super(code);
    }
}
