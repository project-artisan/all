package org.artisan.payload;


import org.artisan.domain.AnswerState;
import org.artisan.domain.TailQuestion;

public record TailQuestionStatusResponse(
        Long interviewId,
        Long interviewQuestionId,
        Long tailQuestionId,
        AnswerState answerState
){

    public static TailQuestionStatusResponse from(TailQuestion tailQuestion) {
        return new TailQuestionStatusResponse(
                tailQuestion.getInterview().getId(),
                tailQuestion.getInterviewQuestion().getId(),
                tailQuestion.getId(),
                tailQuestion.getAnswer().state()
        );
    }
}
