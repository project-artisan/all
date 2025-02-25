package org.artisan.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.artisan.domain.InterviewProgress;
import org.artisan.domain.InterviewSetting;

@Builder
public record InterviewCreateRequest(
        @NotNull Long questionSetId,
        @NotNull Integer tailQuestionDepth,
        @NotNull Integer totalProblemCount,
        @NotNull Integer timeToAnswer,
        @NotNull Integer timeToThink
) {

    public InterviewProgress toProgress(){
        return InterviewProgress.of(totalProblemCount);
    }

    public InterviewSetting toSetting(){
        return new InterviewSetting(
                tailQuestionDepth,
                timeToThink,
                timeToAnswer
        );
    }

}
