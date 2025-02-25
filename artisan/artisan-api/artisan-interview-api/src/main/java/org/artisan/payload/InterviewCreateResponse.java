package org.artisan.payload;

import org.artisan.domain.Interview;

public record InterviewCreateResponse(
        Long interviewId
) {
    public static InterviewCreateResponse from(Interview interview) {
        return new InterviewCreateResponse(interview.getId());
    }
}
