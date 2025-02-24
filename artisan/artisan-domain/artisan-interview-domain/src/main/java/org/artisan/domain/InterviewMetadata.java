package org.artisan.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record InterviewMetadata (
        String title
){
}
