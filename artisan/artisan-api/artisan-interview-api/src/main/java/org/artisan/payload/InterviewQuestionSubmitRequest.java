package org.artisan.payload;

import org.artisan.domain.Answer;
import org.artisan.domain.AnswerState;

public record InterviewQuestionSubmitRequest(
        Long interviewId,
        Long interviewQuestionId,
        AnswerState answerState,
        Integer timeToAnswer,
        String answerContent
){
    public Answer toAnswer() {

        var state = answerState == AnswerState.COMPLETE ? AnswerState.PROGRESS : answerState;

        return Answer.of(state, answerContent, timeToAnswer);
    }
}
