package org.artisan.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import org.artisan.domain.file.File;

@Embeddable
public record MemberProfile(
        @Column(nullable = false) String nickname,
        @Embedded File profileImage
){
}
