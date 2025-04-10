package org.artisan.payload;

import org.artisan.domain.InterviewScore;

public record ScoreGroup(
        int success,
        int pass,
        int fail
){

    public static ScoreGroup from(InterviewScore interviewScore) {
        return new ScoreGroup(
                interviewScore.getSuccess(),
                interviewScore.getPass(),
                interviewScore.getFail()
        );
    }
}
