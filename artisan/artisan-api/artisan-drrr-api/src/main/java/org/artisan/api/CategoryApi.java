package org.artisan.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.payload.SearchCategoryRequest;
import org.artisan.payload.SearchCategoryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryApi {

    @GetMapping
    public Slice<SearchCategoryResponse> find(
            @ModelAttribute SearchCategoryRequest request,
            Pageable pageable
    ) {
        return null;
    }
}
