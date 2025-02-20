package org.artisan.exception;

import org.artisan.core.CoreException;
import org.artisan.core.CoreExceptionCode;

public class JwtExpiredException extends CoreException {

    public JwtExpiredException(CoreApiExceptionCode code) {
        super(code);
    }
}
