package org.artisan.exception;

import lombok.extern.slf4j.Slf4j;
import org.artisan.core.exception.CoreException;
import org.artisan.core.exception.CoreExceptionCode;
import org.artsian.web.core.exception.HttpExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CoreApiExceptionHandler {


    @ExceptionHandler(CoreException.class)
    ResponseEntity<ExceptionResponse> corExceptionHandler(CoreException coreException) {
        var code = coreException.getCoreExceptionCode();

        printLog(coreException);

        var exceptionResponse = new ExceptionResponse(code.getExceptionCode(), code.getMessage());

        return switch (code) {
            case HttpExceptionCode httpExceptionCode -> ResponseEntity.status(httpExceptionCode.getHttpStatus()).body(exceptionResponse);
            case CoreExceptionCode _ -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        };

    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionResponse> exceptionResponseResponseEntity(Exception e){

        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage(), e);

        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse("E00", "서버에서 알 수 없는 에러가 발생했습니다."));

    }


    private void printLog(CoreException e) {
        var exceptionCode = e.getCoreExceptionCode();

        switch (exceptionCode) {
            case HttpExceptionCode httpExceptionCode -> printLog(e, httpExceptionCode);
            case CoreExceptionCode coreExceptionCode -> {}
        }
    }



    private void printLog(CoreException e, HttpExceptionCode code) {
        switch (code.getLevel()){
            case ERROR -> log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage(), e);
            case WARN -> log.warn("{} : {}", e.getClass().getSimpleName(), e.getMessage(), e);
            default -> log.info("{} : {}", e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public record ExceptionResponse(String code, String message){

    }

}
