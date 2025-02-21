package org.artisan.exernal.client;


import static org.springframework.http.MediaType.APPLICATION_JSON;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubApiClient {

    private final RestClient restClient;
    private final GithubApiProperty githubApiProperty;
    private final Status4xxErrorHandler status4xxErrorHandler;
    private final Status5xxErrorHandler status5xxErrorHandler;

    private GithubAccessTokenResponse requestAccessToken(String code, String redirectUrl) {
        return restClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(GithubAccessTokenRequest.create(githubApiProperty, code, redirectUrl))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, status4xxErrorHandler)
                .onStatus(HttpStatusCode::is5xxServerError, status5xxErrorHandler)
                .body(GithubAccessTokenResponse.class);
    }

    public GithubProfileResponse requestProfile(String code, String redirectUrl) {
        final var response = this.requestAccessToken(code, redirectUrl);
        return restClient.get()
                .uri("https://api.github.com/user")
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",response.accessToken()))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, status4xxErrorHandler)
                .onStatus(HttpStatusCode::is5xxServerError, status5xxErrorHandler)
                .body(GithubProfileResponse.class);
    }
}

