package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record InterviewProgress(
        @Column(nullable = false, name = "interview_index") int index,
        @Column(nullable = false, name = "interview_size") int size,
        @Enumerated(EnumType.STRING) InterviewProgress status
){
}
