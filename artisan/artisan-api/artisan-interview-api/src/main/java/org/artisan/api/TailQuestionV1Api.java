package org.artisan.api;

import lombok.RequiredArgsConstructor;
import org.artisan.attributes.Auth;
import org.artisan.attributes.MemberOnly;
import org.artisan.core.User;
import org.artisan.payload.TailQuestionStatusResponse;
import org.artisan.payload.TailQuestionSubmitV1Request;
import org.artisan.service.InterviewSubmitService;
import org.artisan.service.TailQuestionService;
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
@RequestMapping("/api/v1/tail-questions")
public class TailQuestionV1Api {

    private final InterviewSubmitService interviewSubmitService;
    private final TailQuestionService tailQuestionService;

    @MemberOnly
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping( "/{tailQuestionId}/submit")
    public void submit(
            @Auth User user,
            @PathVariable Long tailQuestionId,
            @RequestBody TailQuestionSubmitV1Request request
    ) {
        interviewSubmitService.submit(user, tailQuestionId, request);
    }

    @MemberOnly
    @GetMapping("/{tailQuestionId}/state")
    public TailQuestionStatusResponse getState(
            @Auth User user,
            @PathVariable Long tailQuestionId
    ){
        return TailQuestionStatusResponse.from(tailQuestionService.checkState(user, tailQuestionId));
    }
}
