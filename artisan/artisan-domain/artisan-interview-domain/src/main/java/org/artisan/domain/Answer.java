package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record Answer(
        @Enumerated(EnumType.STRING)
        @Column(name = "answer_state")
        AnswerState state,
        @Column(name = "answer_content", length = 500) String content,
        @Column(name = "time_to_answer") Integer timeToAnswer
){

    public static Answer init(){
        return new Answer(
                AnswerState.INIT,
                null,
                null
        );
    }

    public static Answer of(AnswerState answerState, String content, Integer timeToAnswer) {
        return new Answer(answerState, content, timeToAnswer);
    }

    public Answer complete() {
        return new Answer(AnswerState.COMPLETE, content, timeToAnswer) ;
    }
}
