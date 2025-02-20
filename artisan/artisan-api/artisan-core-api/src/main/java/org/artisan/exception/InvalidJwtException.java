package org.artisan.exception;

import org.artisan.core.CoreException;

public class InvalidJwtException extends CoreException {
    public InvalidJwtException(CoreApiExceptionCode coreApiExceptionCode) {
        super(coreApiExceptionCode);
    }
}
