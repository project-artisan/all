package org.artisan.payload;


import org.artisan.domain.QuestionSet;

public record QuestionSetSearchResponse(
        Long questionSetId,
        String title,
        String description,
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
