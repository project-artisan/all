package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.artisan.core.domain.BaseEntity;

@Entity
@Table(name = "tail_questions")
public class TailQuestion extends BaseEntity {


    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Interview interview;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private InterviewQuestion interviewQuestion;

    @Column(nullable = false)
    private String question;

    @Embedded
    private AIFeedback aiFeedback;

    @Embedded
    private Answer answer = Answer.init();


}
