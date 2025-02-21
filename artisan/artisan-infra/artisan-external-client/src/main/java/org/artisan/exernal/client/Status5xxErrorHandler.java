package org.artisan.exernal.client;

import org.artsian.web.exception.InternalServerError;
import org.springframework.stereotype.Component;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

@Component
public class Status5xxErrorHandler implements ErrorHandler {

    @Override
    public void handle(HttpRequest request, ClientHttpResponse response) {
        throw new InternalServerError(GithubClientExceptionCode.INVALID_OAUTH_SERVER);
    }
}
