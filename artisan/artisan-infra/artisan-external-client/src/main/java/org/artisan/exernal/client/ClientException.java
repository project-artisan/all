package org.artisan.exernal.client;

import org.artisan.core.exception.CoreException;
import org.artisan.core.exception.CoreExceptionCode;

public class ClientException extends CoreException {

    public ClientException(CoreExceptionCode coreExceptionCode) {
        super(coreExceptionCode);
    }
}
