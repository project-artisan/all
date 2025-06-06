package org.artisan.api;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.artisan.attributes.Auth;
import org.artisan.attributes.MemberOnly;
import org.artisan.core.User;
import org.artisan.payload.InterviewCreateRequest;
import org.artisan.payload.InterviewCreateResponse;
import org.artisan.payload.InterviewDetailResponse;
import org.artisan.payload.InterviewQuestionResponse;
import org.artisan.payload.InterviewResultResponse;
import org.artisan.payload.InterviewSubmitRequest;
import org.artisan.service.FeedbackService;
import org.artisan.service.InterviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews")
public class InterviewApi {

    private final InterviewService interviewService;
    private final FeedbackService feedbackService;

    @MemberOnly
    @PostMapping
    public InterviewCreateResponse createInterview(
            @Auth User user,
            @RequestBody InterviewCreateRequest request
    ) {
        var interview = interviewService.createNewInterview(
                user,
                request.questionSetId(),
                request.toProgress(),
                request.toSetting()
        );

        return InterviewCreateResponse.from(interview);
    }

    @MemberOnly
    @GetMapping("/{interviewId}")
    public InterviewDetailResponse getDetail(
            @Auth User user,
            @NotNull @PathVariable("interviewId") Long interviewId
    ) {
        return InterviewDetailResponse.from(interviewService.getDetail(user, interviewId));
    }

    @MemberOnly
    @GetMapping("/{interviewId}/result")
    public InterviewResultResponse getResult(
            @Auth User user,
            @PathVariable Long interviewId
    ){
        return InterviewResultResponse.from(interviewService.getInterviewResult(user, interviewId));
    }

    @MemberOnly
    @GetMapping("/{interviewId}/current/problem")
    public InterviewQuestionResponse getCurrentProblem(
            @Auth User user,
            @NotNull @PathVariable("interviewId") Long interviewId
    ) {
        var currentInterview = interviewService.loadByCurrentProblem(user, interviewId);

        return InterviewQuestionResponse.from(currentInterview);
    }

    @MemberOnly
    @PostMapping("/{interviewId}/submit")
    public InterviewSubmitResponse submit(
            @Auth User user,
            @PathVariable Long interviewId,
            @RequestBody InterviewSubmitRequest request
    ) {
        var tailQuestion = feedbackService.submit(user, interviewId, request);
        return InterviewSubmitResponse.from(tailQuestion);
    }


    @MemberOnly
    @GetMapping("/me")
    public PagedModel<MyInterviewResponse> getMe(
            @Auth User user,
            Pageable pageable
    ) {
        return new PagedModel<>(interviewService.search(user,pageable)
                .map(MyInterviewResponse::from));
    }


}
