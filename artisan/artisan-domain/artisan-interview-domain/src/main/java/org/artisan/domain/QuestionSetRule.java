package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record QuestionSetRule(
        @Column(nullable = false) int tailQuestionDepth,
        @Column(nullable = false) int timeToThink,
        @Column(nullable = false) int timeToAnswer
){
}
