package org.artisan.domain;

import org.jspecify.annotations.Nullable;

public record CurrentInterview (
        Interview interview,
        @Nullable InterviewQuestion interviewQuestion
){

}
