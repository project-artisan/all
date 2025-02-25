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
import lombok.Setter;
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
    }

    public InterviewQuestion getCurrentProblem() {
        // TODO index 범위 조절
        return interviewQuestions.get(progress.getIndex());
    }

    @Nullable
    public TailQuestion submit(
            Answer answer,
            AIFeedback aiFeedback
    ) {
        var interviewQuestion = this.getCurrentProblem();

        progress.next();

        return interviewQuestion.submit(answer, aiFeedback);
    }

    public void setInterviewQuestions(List<InterviewQuestion> interviewQuestions) {
        // TODO 이거 고치자..
        this.interviewQuestions = new InterviewQuestions(interviewQuestions);
    }

}
