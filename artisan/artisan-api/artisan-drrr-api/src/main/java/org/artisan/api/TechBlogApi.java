package org.artisan.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.artisan.payload.GroupingCategoryCountResponse;
import org.artisan.payload.SearchTechBlogPostResponse;
import org.artisan.payload.TechBlogListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blogs")
public class TechBlogApi {


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


}
