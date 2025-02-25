package org.artisan.api;

import java.util.Objects;
import org.artisan.domain.TailQuestion;
import org.jspecify.annotations.Nullable;

public record InterviewSubmitResponse(
        Long tailQuestionId,
        String question
){
    public static InterviewSubmitResponse from(@Nullable TailQuestion tailQuestion) {

        if(Objects.isNull(tailQuestion)) {
            return new InterviewSubmitResponse(null, null);
        }

        return new InterviewSubmitResponse(
                tailQuestion.getId(),
                tailQuestion.getQuestion()
        );
    }
}
