package org.artisan.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.artisan.attributes.Auth;
import org.artisan.core.User;
import org.artisan.domain.TechBlogPost;
import org.artisan.payload.GroupingCategoryCountResponse;
import org.artisan.payload.SearchTechBlogPostRequest;
import org.artisan.payload.SearchTechBlogPostResponse;
import org.artisan.payload.TechBlogListResponse;
import org.artisan.service.TechBlogService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blogs")
public class TechBlogApi {

    private final TechBlogService techBlogService;


    @GetMapping
    Slice<SearchTechBlogPostResponse> getPosts(
            @Auth User user,
            @ModelAttribute SearchTechBlogPostRequest searchTechBlogPostRequest,
            Pageable pageable
    ){
        return techBlogService.readAll(searchTechBlogPostRequest.title(), pageable)
                .map(SearchTechBlogPostResponse::from);
    }

/*
    @GetMapping
    public TechBlogListResponse collectBlogs() {
        return null;
    }

    @GetMapping("/{blogId}/categories")
    public List<GroupingCategoryCountResponse> getBlogCategories(@PathVariable("blogId") Long blogId) {
        return null;
    }

    @GetMapping("/{blogId}/posts")
    Slice<SearchTechBlogPostResponse> getBlogPosts(
            @PathVariable Long blogId,
            Pageable pageable
    ) {
        return null;
    }

*/

}
