package org.artisan.exception;

import org.artisan.core.exception.CoreException;
import org.artisan.core.exception.CoreExceptionCode;

public class UnAuthorizedException extends CoreException {

    public UnAuthorizedException(CoreExceptionCode coreExceptionCode) {
        super(coreExceptionCode);
    }
}
