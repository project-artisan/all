package org.artisan.payload;

import org.artisan.domain.AnswerState;
import org.artisan.domain.InterviewQuestion;

public record InterviewQuestionStatusResponse(
        Long interviewId,
        Long interviewQuestionId,
        AnswerState answerState
){

    public static InterviewQuestionStatusResponse from(InterviewQuestion interviewQuestion) {
        return new InterviewQuestionStatusResponse(
                interviewQuestion.getInterview().getId(),
                interviewQuestion.getId(),
                interviewQuestion.getAnswer().state()
        );
    }
}
