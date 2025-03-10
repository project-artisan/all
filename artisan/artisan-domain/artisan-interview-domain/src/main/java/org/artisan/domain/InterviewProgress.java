package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.exception.InterviewDomainException;
import org.artisan.exception.InterviewDomainExceptionCode;


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

    public boolean isDone(){
        return status == InterviewStatus.DONE;
    }

    public void next() {
        if(status != InterviewStatus.PROGRESS || index == size) {
            throw new InterviewDomainException(InterviewDomainExceptionCode.INTERVIEW_IS_DONE);
        }

        this.index += 1;

        if(index >= size) {
            this.status = InterviewStatus.DONE;
        }
    }
}
