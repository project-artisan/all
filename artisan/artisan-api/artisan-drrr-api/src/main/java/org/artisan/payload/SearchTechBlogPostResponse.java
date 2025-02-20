package org.artisan.payload;

import java.util.List;
import lombok.Builder;

@Builder
public record SearchTechBlogPostResponse(
        Long id,
        String title,
        String thumbnail,
        String description,
        String link,
        TechBlogCodeResponse techBlog,
        int viewCount,

        List<SearchCategoryResponse> categories,

        boolean hasRead
) {
}
