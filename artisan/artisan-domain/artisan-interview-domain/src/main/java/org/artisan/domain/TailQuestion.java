package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.domain.BaseEntity;
import org.artisan.exception.InterviewDomainException;
import org.artisan.exception.InterviewDomainExceptionCode;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Getter
@Entity
@Table(name = "tail_questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TailQuestion extends BaseEntity {

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Interview interview;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private InterviewQuestion interviewQuestion;

    @Column(nullable = false)
    private String question;

    @Embedded
    private AIFeedback aiFeedback;

    @Embedded
    private Answer answer = Answer.init();

    public TailQuestion(Member member, Interview interview, InterviewQuestion interviewQuestion, String question) {
        this.member = member;
        this.interview = interview;
        this.interviewQuestion = interviewQuestion;
        this.question = question;
    }

    public static TailQuestion of(
            Member member,
            Interview interview,
            InterviewQuestion interviewQuestion,
            String question
    ) {
        return new TailQuestion(
                member,
                interview,
                interviewQuestion,
                question
        );
    }




    @Nullable
    public TailQuestion submit(Answer answer, AIFeedback aiFeedback) {
        switch (answer.state()) {
            case COMPLETE -> this.aiFeedback = aiFeedback;
            case PASS -> {}
            default -> throw new UnsupportedOperationException();
        }

        this.answer = answer;
        return interviewQuestion.createTailQuestion(aiFeedback);
    }

    public void submit(@NonNull Answer answer) {
        if(this.answer.state() != AnswerState.INIT) {
            throw new InterviewDomainException(InterviewDomainExceptionCode.FAIL_SUBMIT_ANSWER_TO_IQ);
        }
        this.answer = answer;
    }

    @Nullable
    public TailQuestion mark(@NonNull AIFeedback aiFeedback) {
        if(answer.state() != AnswerState.PROGRESS) {
            // TODO 유효하지 않은 답변 에러코드 처리
            throw new InterviewDomainException(InterviewDomainExceptionCode.FAIL_INTERVIEW_CREATE);
        }

        this.aiFeedback = aiFeedback;
        this.answer = answer.complete();

        return interviewQuestion.createTailQuestion(aiFeedback);
    }

}

