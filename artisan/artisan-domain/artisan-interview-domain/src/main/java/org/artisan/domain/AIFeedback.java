package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.util.List;
import org.artisan.converter.StringListConverter;

@Embeddable
public record AIFeedback(
        String tailQuestion,
        @Column(length = 2000, name = "feedback_content") String feedback,
        @Column(nullable = false, name = "ai_feedback_score") int score,

        @Column(length = 10000)
        @Convert(converter = StringListConverter.class)
        List<String> referenceLinks

) {
}
