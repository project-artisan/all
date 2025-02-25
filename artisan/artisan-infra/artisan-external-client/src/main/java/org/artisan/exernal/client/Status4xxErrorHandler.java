package org.artisan.exernal.client;

import org.artsian.web.core.exception.BadRequestException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

@Component
public class Status4xxErrorHandler implements ErrorHandler {

    @Override
    public void handle(HttpRequest request, ClientHttpResponse response) {

        throw new BadRequestException(GithubClientExceptionCode.INVALID_OAUTH_CODE);
    }
}
