package org.artisan.payload;


import org.artisan.domain.QuestionSet;

public record QuestionSetSearchResponse(
        Long questionSetId,
        String description,
        String title,
        int count,
        int tailQuestionDepth,
        String thumbnailUrl
){

    public static QuestionSetSearchResponse from(QuestionSet questionSet) {
        return new QuestionSetSearchResponse(
                questionSet.getId(),
                questionSet.getMetadata().title(),
                questionSet.getMetadata().description(),
                questionSet.getMetadata().count(),
                questionSet.getRules().tailQuestionDepth(),
                questionSet.getMetadata().thumbnailUrl().toUrl()
        );
    }
}
