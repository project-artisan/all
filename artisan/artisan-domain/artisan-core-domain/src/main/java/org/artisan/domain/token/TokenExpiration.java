package org.artisan.domain.token;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public record TokenExpiration(
        LocalDateTime expiredAt,
        @Column(nullable = false) boolean isExpired
){

    public static TokenExpiration from(LocalDateTime expiredAt){
        return new TokenExpiration(expiredAt, false);
    }

    public TokenExpiration delete() {
        return new TokenExpiration(expiredAt, true);
    }
}
