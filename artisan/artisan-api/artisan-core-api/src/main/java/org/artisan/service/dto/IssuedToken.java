package org.artisan.service.dto;

public record IssuedToken(
        String accessToken,
        String refreshToken
){
}
