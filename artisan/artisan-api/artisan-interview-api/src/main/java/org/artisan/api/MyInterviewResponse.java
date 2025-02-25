package org.artisan.api;

import java.time.LocalDateTime;
import org.artisan.domain.Interview;
import org.artisan.domain.InterviewStatus;

public record MyInterviewResponse(
        Long interviewId,
        String title,
        InterviewStatus interviewStatus,
        int questionCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
    public static MyInterviewResponse from(Interview interview) {
        return new MyInterviewResponse(
                interview.getId(),
                interview.getMetadata().title(),
                interview.getProgress().getStatus(),
                interview.getProgress().getSize(),
                interview.getCreatedAt(),
                interview.getUpdatedAt()
        );
    }
}
