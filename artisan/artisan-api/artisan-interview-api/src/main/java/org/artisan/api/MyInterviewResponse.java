package org.artisan.api;

import java.time.LocalDateTime;
import org.artisan.domain.Interview;
import org.artisan.domain.InterviewStatus;
import org.artisan.payload.ScoreGroup;

public record MyInterviewResponse(
        Long interviewId,
        String title,
        InterviewStatus interviewStatus,
        int questionCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ScoreGroup scoreGroup

){
    public static MyInterviewResponse from(Interview interview) {
        return new MyInterviewResponse(
                interview.getId(),
                interview.getMetadata().title(),
                interview.getProgress().getStatus(),
                interview.getProgress().getSize(),
                interview.getCreatedAt(),
                interview.getUpdatedAt(),
                ScoreGroup.from(interview.getScoreGroup())
        );
    }
}
