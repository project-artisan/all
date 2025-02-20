package org.artisan.exception;

import org.artisan.core.CoreException;
import org.artisan.core.CoreExceptionCode;

public class UnAuthorizedException extends CoreException {

    public UnAuthorizedException(CoreExceptionCode coreExceptionCode) {
        super(coreExceptionCode);
    }
}
