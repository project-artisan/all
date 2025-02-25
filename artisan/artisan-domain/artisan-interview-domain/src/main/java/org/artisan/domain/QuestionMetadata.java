package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.artisan.domain.file.ExternalURL;

@Embeddable
public record QuestionMetadata(
        @Column(nullable = false) String content,
        @Column(nullable = false) int sequence,
        @Column(nullable = false, name = "question_count") int count
) {
}
