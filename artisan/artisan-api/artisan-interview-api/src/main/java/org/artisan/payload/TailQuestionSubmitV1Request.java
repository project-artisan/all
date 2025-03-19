package org.artisan.payload;

import jakarta.validation.constraints.NotNull;
import org.artisan.domain.Answer;
import org.artisan.domain.AnswerState;

public record TailQuestionSubmitV1Request(
        @NotNull Long interviewQuestionId,
        @NotNull AnswerState answerState,
        @NotNull Integer timeToAnswer,
        @NotNull String answerContent
){


    public Answer toAnswer() {
        var state = answerState == AnswerState.COMPLETE ? AnswerState.PROGRESS : answerState;

        return Answer.of(state, answerContent, timeToAnswer);
    }
}
