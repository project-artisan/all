package org.artisan.payload;

import java.util.List;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.artisan.domain.Answer;
import org.artisan.domain.AnswerState;

@Slf4j
@Builder
public record InterviewSubmitRequest(
        Long interviewQuestionId,
        AnswerState answerState,
        Integer timeToAnswer,
        String answerContent
) {
    public Answer toAnswer() {
        return Answer.of(answerState, answerContent, timeToAnswer);
    }
}
