package org.artisan.service;

import lombok.RequiredArgsConstructor;
import org.artisan.core.User;
import org.artisan.domain.TailQuestionRepository;
import org.artisan.payload.InterviewQuestionSubmitRequest;
import org.artisan.payload.TailQuestionSubmitV1Request;
import org.artisan.service.FeedbackEventListener.InterviewSubmitEvent;
import org.artisan.service.FeedbackEventListener.TailQuestionSubmitEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewSubmitService {

    private final TailQuestionRepository tailQuestionRepository;
    private final InterviewQuestionService interviewQuestionService;

    private final ApplicationEventPublisher eventPublisher;

    public void submit(User user, Long interviewQuestionId, InterviewQuestionSubmitRequest request) {
        // 여기서 대기로 바뀌어야 함
        var interviewQuestion = interviewQuestionService.submit(user, request.interviewId(), request.toAnswer());
        var question = interviewQuestion.getQuestion().getMetadata().content();

        var interviewSubmitEvent = new InterviewSubmitEvent(
                user,
                request.interviewId(),
                question,
                interviewQuestionId,
                request.toAnswer()
        );

        eventPublisher.publishEvent(interviewSubmitEvent);
    }

    public void submit(User user, Long tailQuestionId, TailQuestionSubmitV1Request request) {
        var tailQuestion = tailQuestionRepository.getByIdAndMemberId(tailQuestionId, user.id());
        var question = tailQuestion.getQuestion();

        var tailQuestionSubmitEvent = new TailQuestionSubmitEvent(
                user,
                request.interviewQuestionId(),
                question,
                tailQuestionId,
                request.toAnswer()
        );

        eventPublisher.publishEvent(tailQuestionSubmitEvent);

    }
}
