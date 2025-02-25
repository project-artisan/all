package org.artisan.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CoreException extends RuntimeException {

    private final CoreExceptionCode coreExceptionCode;

    public CoreException(final String message) {
        super(message);

        coreExceptionCode = new CoreExceptionCode() {

            @Override
            public String getIdentifier() {
                return "E500";
            }

            @Override
            public String getPrefix() {
                return "DEFAULT";
            }

            @Override
            public String getMessage() {
                return "시스템에 문제가 발생했습니다";
            }
        };
    }


}
