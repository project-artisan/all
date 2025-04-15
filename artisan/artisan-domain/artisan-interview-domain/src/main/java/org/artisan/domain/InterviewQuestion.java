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
@Table(name = "interview_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewQuestion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    private Question question;

    @Embedded
    private Answer answer = Answer.init();

    @Embedded
    private AIFeedback aiFeedback;

    @Embedded
    private TailQuestions tailQuestions;

    @Column(nullable = false)
    private int remainTailQuestionCount;


    private InterviewQuestion(Interview interview, Member member, Question question, int tailQuestionDepth) {
        this.interview = interview;
        this.member = member;
        this.question = question;
        this.answer = Answer.init();
        this.remainTailQuestionCount = tailQuestionDepth;
    }

    public static InterviewQuestion of(
            Interview interview,
            Member member,
            Question question,
            int tailQuestionDepth
    ) {
        return new InterviewQuestion(interview, member, question, tailQuestionDepth);
    }


    public void submit(@NonNull Answer answer) {
        if(this.answer.state() != AnswerState.INIT) {
            // TODO 유요하지 않은 답변 처리
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

        return createTailQuestion(aiFeedback);
    }

    @Nullable
    public TailQuestion submit(Answer answer, AIFeedback aiFeedback) {
        this.answer = answer;
        this.aiFeedback = aiFeedback;

        if(answer.state() == AnswerState.PROGRESS) {
            return null;
        }

        return createTailQuestion(aiFeedback);
    }

    @Nullable
    public TailQuestion createTailQuestion(AIFeedback aiFeedback){
        if(remainTailQuestionCount == 0) {
            return null;
        }
        consumeTailQuestionCount();
        return TailQuestion.of(this.member, this.interview, this, aiFeedback.tailQuestion());
    }

    public void consumeTailQuestionCount(){
        remainTailQuestionCount--;
    }
}
