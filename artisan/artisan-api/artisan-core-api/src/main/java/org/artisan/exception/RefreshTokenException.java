package org.artisan.exception;

import org.artisan.core.exception.CoreException;

public class RefreshTokenException extends CoreException  {
    public RefreshTokenException(CoreApiExceptionCode coreApiExceptionCode) {
        super(coreApiExceptionCode);
    }
}
