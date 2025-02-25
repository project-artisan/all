package org.artisan.payload;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import org.artisan.domain.Answer;
import org.artisan.domain.AnswerState;

@Builder
public record TailQuestionSubmitRequest(
        @NotNull Long interviewQuestionId,
        @NotNull Long tailQuestionId,
        @NotNull AnswerState answerState,
        @NotNull Integer timeToAnswer,
        @NotNull String answerContent

) {
    public Answer toAnswer(){
        return Answer.of(answerState, answerContent, timeToAnswer);
    }
}