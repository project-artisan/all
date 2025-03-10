package org.artisan.exception;

import org.artisan.core.exception.CoreException;
import org.artisan.core.exception.CoreExceptionCode;
import org.artsian.web.core.exception.HttpExceptionCode;

public class DrrrDomainException extends CoreException  {

    public DrrrDomainException(HttpExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
