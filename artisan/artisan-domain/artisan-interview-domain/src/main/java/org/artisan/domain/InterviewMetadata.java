package org.artisan.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record InterviewMetadata (
        String title
){
    public static InterviewMetadata from(QuestionSet questionSet) {
        return new InterviewMetadata(questionSet.getMetadata().title());
    }
}
