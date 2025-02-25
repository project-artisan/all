package org.artisan.domain;

import jakarta.persistence.Embeddable;
import org.artisan.domain.file.ExternalURL;

@Embeddable
public record QuestionSetMetadata(
        String title,
        String description,
        ExternalURL thumbnailUrl,
        int count
){
}
