package org.artisan.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.attributes.Auth;
import org.artisan.attributes.MemberOnly;
import org.artisan.core.User;
import org.artisan.payload.TailQuestionSubmitRequest;
import org.artisan.payload.TailQuestionSubmitResponse;
import org.artisan.service.FeedbackService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tail-questions")
public class TailQuestionApi {
    private final FeedbackService feedbackService;

    @MemberOnly
    @PostMapping("/{tailQuestionId}/submit")
    public TailQuestionSubmitResponse submit(
            @Auth User user,
            @PathVariable Long tailQuestionId,
            @RequestBody TailQuestionSubmitRequest request
    ) {
        var submitResult = feedbackService.submit(user, tailQuestionId, request);
        return TailQuestionSubmitResponse.from(submitResult);
    }
}

