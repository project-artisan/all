package org.artisan.api;

import lombok.RequiredArgsConstructor;
import org.artisan.attributes.Auth;
import org.artisan.attributes.MemberOnly;
import org.artisan.core.User;
import org.artisan.payload.InterviewQuestionStatusResponse;
import org.artisan.payload.InterviewQuestionSubmitRequest;
import org.artisan.service.InterviewQuestionService;
import org.artisan.service.InterviewSubmitService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview-questions")
public class InterviewQuestionApi {

    private final InterviewQuestionService interviewQuestionService;
    private final InterviewSubmitService interviewSubmitService;


    @MemberOnly
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/{interviewQuestionId}/submit")
    public void submit(
            @Auth User user,
            @PathVariable Long interviewQuestionId,
            @RequestBody InterviewQuestionSubmitRequest request
    ) {

        interviewSubmitService.submit(user, interviewQuestionId,  request);
    }

    @MemberOnly
    @GetMapping("{interviewQuestionId}/state")
    public InterviewQuestionStatusResponse getState(
            @Auth User user,
            @PathVariable Long interviewQuestionId
    ){
        return InterviewQuestionStatusResponse.from(interviewQuestionService.checkState(user, interviewQuestionId));
    }

}
