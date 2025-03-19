package org.artisan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.ai.client.AIClient;
import org.artisan.ai.client.AiFeedbackRequest;
import org.artisan.core.User;
import org.artisan.domain.AIFeedback;
import org.artisan.domain.Answer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackEventListener {

    private final InterviewQuestionService interviewQuestionService;

    private final ApplicationEventPublisher publisher;
    private final AIClient aiClient;
    private final TailQuestionService tailQuestionService;

    @EventListener
    @Async("asyncInterviewExecutor")
    public void submit(InterviewSubmitEvent event){
        var feedbackResponse = aiClient.execute(new AiFeedbackRequest(event.question, event.answer.content()));

        var interviewMarkEvent = new InterviewMarkEvent(
                event.user,
                event.interviewId,
                event.interviewQuestion,
                new AIFeedback(
                    feedbackResponse.tailQuestion(),
                    feedbackResponse.feedback(),
                    feedbackResponse.score(),
                    feedbackResponse.referenceLinks()
                )
        );

        log.info("call event");
        publisher.publishEvent(interviewMarkEvent);
    }


    @EventListener
    @Async("asyncInterviewExecutor")
    public void mark(InterviewMarkEvent event){
        log.info("execute mark {}", event);
        interviewQuestionService.mark(event.user(), event.interviewId, event.feedback);
    }

    @EventListener
    @Async("asyncInterviewExecutor")
    public void submit(TailQuestionSubmitEvent event) {
        var feedbackResponse = aiClient.execute(new AiFeedbackRequest(event.question, event.answer.content()));

        var tailQuestionMarkEvent = new TailQuestionMarkEvent(
                event.user,
                event.interviewQuestionId,
                event.tailQuestionId,
               new AIFeedback(
                    feedbackResponse.tailQuestion(),
                    feedbackResponse.feedback(),
                    feedbackResponse.score(),
                    feedbackResponse.referenceLinks()
                )

        );

        publisher.publishEvent(tailQuestionMarkEvent);
    }

    @EventListener
    @Async("asyncInterviewExecutor")
    public void mark(TailQuestionMarkEvent event) {
        tailQuestionService.mark(event.user(), event.tailQuestionId, event.feedback);
    }


    public record InterviewMarkEvent(
            User user,
            Long interviewId,
            Long interviewQuestion,
            AIFeedback feedback
    ){}
    public record InterviewSubmitEvent(
            User user,
            Long interviewId,
            String question,
            Long interviewQuestion,
            Answer answer
    ){ }

    public record TailQuestionSubmitEvent(
            User user,
            Long interviewQuestionId,
            String question,
            Long tailQuestionId,
            Answer answer
    ) {}


    public record TailQuestionMarkEvent(
            User user,
            Long interviewQuestionId,
            Long tailQuestionId,
            AIFeedback feedback
    ){

    }
}
