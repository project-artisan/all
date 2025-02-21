package org.artisan.exernal.client;

import org.artisan.core.CoreException;
import org.artisan.core.CoreExceptionCode;

public class ClientException extends CoreException {

    public ClientException(CoreExceptionCode coreExceptionCode) {
        super(coreExceptionCode);
    }
}
