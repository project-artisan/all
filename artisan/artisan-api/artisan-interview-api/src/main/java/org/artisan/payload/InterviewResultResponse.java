package org.artisan.payload;

import java.util.List;
import org.artisan.domain.AnswerState;
import org.artisan.domain.Interview;
import org.artisan.domain.InterviewQuestion;
import org.artisan.domain.InterviewQuestions;
import org.artisan.domain.InterviewStatus;
import org.artisan.domain.TailQuestion;
import org.artisan.domain.TailQuestions;

public record InterviewResultResponse(
        Long interviewId,
        InterviewStatus interviewState,
        String title,
        List<InterviewQuestionResult> interviewQuestions

){

    public static InterviewResultResponse from(Interview interviewResult) {
        return new InterviewResultResponse(
                interviewResult.getId(),
                interviewResult.getProgress().getStatus(),
                interviewResult.getMetadata().title(),
                InterviewQuestionResult.from(interviewResult.getInterviewQuestions())
        );
    }

    public record InterviewQuestionResult(
            Long interviewQuestionId,
            AnswerState answerState,
            String question,
            String answer,
            List<String> referenceLinks,
            String feedback,
            int remainTailQuestionCount,
            int score,
            List<TailQuestionResult> tailQuestions

    ) {
        public static List<InterviewQuestionResult> from(InterviewQuestions interviewQuestions) {
            return interviewQuestions.getValue()
                    .stream()
                    .map(InterviewQuestionResult::from)
                    .toList();
        }

        public static InterviewQuestionResult from(InterviewQuestion interviewQuestion){
            return new InterviewQuestionResult(
                    interviewQuestion.getId(),
                    interviewQuestion.getAnswer().state(),
                    interviewQuestion.getQuestion().getMetadata().content(),
                    interviewQuestion.getAnswer().content(),
                    interviewQuestion.getAiFeedback().referenceLinks(),
                    interviewQuestion.getAiFeedback().feedback(),
                    interviewQuestion.getRemainTailQuestionCount(),
                    interviewQuestion.getAiFeedback().score(),
                    TailQuestionResult.from(interviewQuestion.getTailQuestions())
            );
        }
    }

    public record TailQuestionResult(
            Long interviewQuestionId,
            Long tailQuestionId,
            AnswerState answerState,
            String question,
            String answer,
            List<String> referenceLinks,
            String feedback
    ){

        public static List<TailQuestionResult> from(TailQuestions tailQuestions) {
            return tailQuestions.getValue()
                    .stream()
                    .map(TailQuestionResult::from)
                    .toList();
        }

        public static TailQuestionResult from(TailQuestion tailQuestion) {
            return new TailQuestionResult(
                    tailQuestion.getInterviewQuestion().getId(),
                    tailQuestion .getId(),
                    tailQuestion.getAnswer().state(),
                    tailQuestion.getQuestion(),
                    tailQuestion.getAnswer().content(),
                    tailQuestion.getAiFeedback().referenceLinks(),
                    tailQuestion.getAiFeedback().feedback()
            );
        }
    }
}
