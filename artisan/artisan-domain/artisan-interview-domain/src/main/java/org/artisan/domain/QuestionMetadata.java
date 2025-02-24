package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record QuestionMetadata(
        @Column(nullable = false) String content,
        @Column(nullable = false) int sequence
) {
}
