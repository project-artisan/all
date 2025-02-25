package org.artisan.payload;

import java.util.List;
import org.artisan.domain.AnswerState;
import org.artisan.domain.Interview;
import org.artisan.domain.InterviewQuestion;
import org.artisan.domain.InterviewQuestions;
import org.artisan.domain.InterviewStatus;

public record InterviewDetailResponse(
        Long interviewId,
        InterviewStatus interviewState,
        String question,
        List<InterviewQuestionDetail> interviewQuestions
) {

    public static InterviewDetailResponse from(Interview interview) {
        return new InterviewDetailResponse(
                interview.getId(),
                interview.getProgress().getStatus(),
                interview.getMetadata().title(),
                InterviewQuestionDetail.from(interview.getInterviewQuestions())
        );
    }

    public record InterviewQuestionDetail(
            Long interviewQuestionId,
            AnswerState answerState,
            String question,
            String answer,
            List<String> referenceLinks,
            String feedback,
            int remainTailQuestionCount

    ) {
        public static List<InterviewQuestionDetail> from(InterviewQuestions interviewQuestions) {
            return interviewQuestions.getValue()
                    .stream()
                    .map(InterviewQuestionDetail::from)
                    .toList();
        }

        public static InterviewQuestionDetail from(InterviewQuestion interviewQuestion){
            return new InterviewQuestionDetail(
                    interviewQuestion.getId(),
                    interviewQuestion.getAnswer().state(),
                    interviewQuestion.getQuestion().getMetadata().content(),
                    interviewQuestion.getAnswer().content(),
                    interviewQuestion.getAiFeedback().referenceLinks(),
                    interviewQuestion.getAiFeedback().feedback(),
                    interviewQuestion.getRemainTailQuestionCount()
            );
        }
    }
}
