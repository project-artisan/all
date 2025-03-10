package org.artisan.exception;

import org.artisan.core.exception.CoreException;
import org.artsian.web.core.exception.HttpExceptionCode;

public class InterviewDomainException extends CoreException  {

    public InterviewDomainException (HttpExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
