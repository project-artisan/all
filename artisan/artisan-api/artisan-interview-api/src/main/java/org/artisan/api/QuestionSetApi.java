package org.artisan.api;

import lombok.RequiredArgsConstructor;
import org.artisan.attributes.MemberOnly;
import org.artisan.payload.QuestionSetSearchResponse;
import org.artisan.service.QuestionSetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question-sets")
public class QuestionSetApi {

    private final QuestionSetService questionSetService;

    @GetMapping
    public PagedModel<QuestionSetSearchResponse> getQuestionSets(
            Pageable pageable
    ){
        return new PagedModel<>(questionSetService.find(pageable)
                .map(QuestionSetSearchResponse::from));
    }

}
