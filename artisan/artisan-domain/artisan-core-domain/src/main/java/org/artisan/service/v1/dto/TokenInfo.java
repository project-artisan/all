package org.artisan.service.v1.dto;

public record TokenInfo (
        String accessToken,
        String refreshToken
){
}
