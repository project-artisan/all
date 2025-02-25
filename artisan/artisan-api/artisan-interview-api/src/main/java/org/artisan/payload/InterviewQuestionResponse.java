package org.artisan.payload;

import org.artisan.domain.InterviewQuestion;

public record InterviewQuestionResponse(
        Long interviewId,
        Long interviewQuestionId,
        String question,
        int index,
        int size,
        int remainTailQuestionCount
) {
    public static InterviewQuestionResponse from(InterviewQuestion interviewQuestion) {
        return new InterviewQuestionResponse(
                interviewQuestion.getId(),
                interviewQuestion.getQuestion().getId(),
                interviewQuestion.getQuestion().getMetadata().content(),
                interviewQuestion.getInterview().getProgress().getIndex(),
                interviewQuestion.getInterview().getProgress().getSize(),
                interviewQuestion.getRemainTailQuestionCount()
        );
    }
}
