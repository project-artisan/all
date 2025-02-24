package org.artisan.domain;

import jakarta.persistence.Column;

public record InterviewSetting(
        @Column(nullable = false) int tailQuestionDepth,
        @Column(nullable = false) int timeToThink,
        @Column(nullable = false) int timeToAnswer
){

    public static InterviewSetting from(QuestionSetRule rule){
        return new InterviewSetting(
                rule.tailQuestionDepth(),
                rule.timeToThink(),
                rule.timeToAnswer()
        );
    }

}
