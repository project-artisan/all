package org.artisan.payload;

import org.artisan.domain.CurrentInterview;
import org.artisan.domain.InterviewStatus;

public record InterviewQuestionResponse(
        Long interviewId,
        InterviewStatus interviewStatus,
        Long interviewQuestionId,
        String question,
        int index,
        int size,
        int remainTailQuestionCount
) {

    public static InterviewQuestionResponse from(CurrentInterview currentInterview) {
        var interviewQuestion  = currentInterview.interviewQuestion();
        var interview = currentInterview.interview();

        if(interviewQuestion == null) {
            return new InterviewQuestionResponse(
                    interview.getId(),
                    interview.getProgress().getStatus(),
                    null,
                    null,
                    0,
                    0,
                    0
            );

        }
        return new InterviewQuestionResponse(
                interview.getId(),
                interview.getProgress().getStatus(),
                interviewQuestion.getQuestion().getId(),
                interviewQuestion.getQuestion().getMetadata().content(),
                interviewQuestion.getInterview().getProgress().getIndex(),
                interviewQuestion.getInterview().getProgress().getSize(),
                interviewQuestion.getRemainTailQuestionCount()
        );
    }
}
