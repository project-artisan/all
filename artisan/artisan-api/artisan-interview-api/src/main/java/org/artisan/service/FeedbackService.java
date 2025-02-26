package org.artisan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.ai.client.AIClient;
import org.artisan.ai.client.AiFeedbackRequest;
import org.artisan.core.User;
import org.artisan.domain.AIFeedback;
import org.artisan.domain.InterviewQuestionRepository;
import org.artisan.domain.TailQuestion;
import org.artisan.domain.TailQuestionRepository;
import org.artisan.payload.InterviewSubmitRequest;
import org.artisan.payload.TailQuestionSubmitRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final AIClient aiClient;
    private final TailQuestionService tailQuestionService;
    private final TailQuestionRepository tailQuestionRepository;
    private final InterviewService interviewService;
    private final InterviewQuestionRepository interviewQuestionRepository;

    public TailQuestion submit(User user, Long interviewId, InterviewSubmitRequest request){
        log.info("{}" ,request);
        var interviewQuestion = interviewQuestionRepository.getById(request.interviewQuestionId());
        var question = interviewQuestion.getQuestion().getMetadata().content();


        var feedbackResponse = aiClient.execute(new AiFeedbackRequest(
                question,
                request.answerContent()
        ));


        var feedback = new AIFeedback(
                feedbackResponse.tailQuestion(),
                feedbackResponse.feedback(),
                feedbackResponse.score(),
                feedbackResponse.referenceLinks()
        );


        return interviewService.submit(user, interviewId, request.toAnswer(), feedback);
    }

    public TailQuestion submit(User user, Long interviewQuestionId, TailQuestionSubmitRequest request) {
        var tailQuestion = tailQuestionRepository.getByIdAndMemberId(request.tailQuestionId(), user.id());
        var question = tailQuestion.getQuestion();

        var feedbackResponse = aiClient.execute(new AiFeedbackRequest(
                question,
                request.answerContent()
        ));

        var feedback = new AIFeedback(
                feedbackResponse.tailQuestion(),
                feedbackResponse.feedback(),
                feedbackResponse.score(),
                feedbackResponse.referenceLinks()
        );

        return tailQuestionService.submit(user, interviewQuestionId, request.toAnswer(), feedback);
    }
}
