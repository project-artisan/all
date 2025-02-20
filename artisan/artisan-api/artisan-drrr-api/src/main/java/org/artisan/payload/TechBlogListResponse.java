package org.artisan.payload;

import java.util.List;

public record TechBlogListResponse(
        List<TechBlogCodeResponse> blogs
) {
}