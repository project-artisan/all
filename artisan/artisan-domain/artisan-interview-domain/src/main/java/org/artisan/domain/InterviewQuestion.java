package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.domain.BaseEntity;
import org.jspecify.annotations.Nullable;

@Getter
@Entity
@Table(name = "interview_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewQuestion extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Embedded
    private Answer answer = Answer.init();

    @Embedded
    private AIFeedback aiFeedback;

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

    @Nullable
    public TailQuestion submit(Answer answer, AIFeedback aiFeedback) {
        this.answer = answer;
        switch (answer.state()){
            case COMPLETE -> this.aiFeedback = aiFeedback;
            case PASS -> {}
            default -> throw new UnsupportedOperationException();
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
