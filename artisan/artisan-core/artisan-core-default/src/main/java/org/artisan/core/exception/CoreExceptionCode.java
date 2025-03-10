package org.artisan.core.exception;

public interface CoreExceptionCode {

    String getIdentifier();


    String getMessage();

    default String getPrefix() {
        throw new UnsupportedOperationException();
    };


    default String getExceptionCode(){
        return getPrefix() + "-" + getIdentifier();
    }

}
