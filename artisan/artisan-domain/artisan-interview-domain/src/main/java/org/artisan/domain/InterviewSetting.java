package org.artisan.domain;

import jakarta.persistence.Column;

public record InterviewSetting(
        @Column(nullable = false) int tailQuestionDepth,
        @Column(nullable = false) int timeToThink,
        @Column(nullable = false) int timeToAnswer
){

    public static InterviewSetting of(
            int tailQuestionDepth,
            int timeToThink,
            int timeToAnswer
    ) {
        return new InterviewSetting(
                tailQuestionDepth,
                timeToThink,
                timeToAnswer
        );
    }

}
