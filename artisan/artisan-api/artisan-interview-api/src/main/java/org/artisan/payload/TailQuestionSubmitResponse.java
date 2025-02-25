package org.artisan.payload;

import org.artisan.domain.TailQuestion;

public record TailQuestionSubmitResponse (
        Long interviewQuestionId,
        Long tailQuestionId,
        String question
){

    public static TailQuestionSubmitResponse from(TailQuestion tailQuestion) {
        if(tailQuestion == null) {
            return null;
        }

        return new TailQuestionSubmitResponse(
                tailQuestion.getInterviewQuestion().getId(),
                tailQuestion.getId(),
                tailQuestion.getQuestion()
        );
    }
}
