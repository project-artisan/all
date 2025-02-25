package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewProgress {

    @Column(nullable = false, name = "interview_index")
    private int index;

    @Column(nullable = false, name = "interview_size")
    private int size;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    public InterviewProgress(
            int index,
            int size,
            InterviewStatus status
    ) {
        this.index = index;
        this.size = size;
        this.status = status;
    }

    public static InterviewProgress of(int size) {
        return new InterviewProgress(0, size, InterviewStatus.PROGRESS);
    }

    public void next() {
        // TODO 검증 로직 추가하기
//        INTERVIEW_STATE_IS_DONE.invokeByCondition(interviewState != InterviewState.PROGRESS);
//        INTERVIEW_STATE_DID_NOT_MATCH.invokeByCondition(index != currentIndex);
//        INTERVIEW_STATE_IS_DONE.invokeByCondition(index == size);

        this.index += 1;

        if(index >= size) {
            this.status = InterviewStatus.DONE;
        }
    }
}
