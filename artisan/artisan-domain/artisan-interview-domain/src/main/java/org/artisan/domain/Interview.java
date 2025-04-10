package org.artisan.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.domain.BaseEntity;
import org.jspecify.annotations.Nullable;

@Getter
@Entity
@Table(name = "interviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interview extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Embedded
    private InterviewMetadata metadata;

    @Embedded
    private InterviewProgress progress;

    @Embedded
    private InterviewSetting setting;

    @Embedded
    private InterviewQuestions interviewQuestions = InterviewQuestions.empty();

    @Embedded
    private InterviewScore scoreGroup;


    public Interview(
            Member member,
            InterviewMetadata metadata,
            InterviewProgress progress,
            InterviewSetting setting
    ) {
        this.member = member;
        this.metadata = metadata;
        this.progress = progress;
        this.setting = setting;
        this.scoreGroup = new InterviewScore();
    }

    @Nullable
    public InterviewQuestion getCurrentProblem() {
        if(progress.isDone()) {
            return null;
        }


        return interviewQuestions.get(progress.getIndex());
    }

    public Long submit(
            Answer answer
    ) {
        var interviewQuestion = interviewQuestions.get(progress.getIndex());

        interviewQuestion.submit(answer);

        return interviewQuestion.getId();
    }

    @Nullable
    public TailQuestion mark(
            AIFeedback aiFeedback
    ) {
        var interviewQuestion = interviewQuestions.get(progress.getIndex());

        progress.next();

        return interviewQuestion.mark(aiFeedback);
    }


    @Nullable
    public TailQuestion submit(
            Answer answer,
            AIFeedback aiFeedback
    ) {
        increaseScore(answer, aiFeedback);

        var interviewQuestion = interviewQuestions.get(progress.getIndex());

        progress.next();

        return interviewQuestion.submit(answer, aiFeedback);
    }

    private void increaseScore(Answer answer, AIFeedback aiFeedback){
        switch(answer.state()) {
            case COMPLETE -> {
                if (aiFeedback.score() >= 80) {
                    scoreGroup.success();
                } else {
                    scoreGroup.fail();
                }
            }
            case PASS -> scoreGroup.pass();
        }
    }

    // 연관관계 편의 메서드 -> 읽기 전용 쿼리 만들어야 할듯..?
    public void setInterviewQuestions(List<InterviewQuestion> interviewQuestions) {
        // TODO 이거 고치자..
        this.interviewQuestions = new InterviewQuestions(interviewQuestions);
    }

}
